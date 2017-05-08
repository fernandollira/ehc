package aiec.br.ehc.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import aiec.br.ehc.R;

/**
 * Created by gilmar on 22/03/17.
 */
public class CepServiceTask extends AsyncTask<String, Void, String> {
    private static String API_URL = "http://api.postmon.com.br/v1/cep/%s";
    private URL url = null;
    private HttpURLConnection httpURLConnection = null;
    private StringBuilder result = null;
    private ProgressDialog dialog;
    private Activity context;

    public CepServiceTask(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.setMessage("Buscando CEP...");
        dialog.setTitle("Aguarde");
        dialog.show();
    }

    protected String doInBackground(String... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        int respCode = -1;
        try {
            url = new URL(String.format(API_URL, params[0].toString()));
            httpURLConnection = (HttpURLConnection) url.openConnection();

            do {
                if (httpURLConnection != null) {
                    respCode = httpURLConnection.getResponseCode();
                }
            } while (respCode == -1);

            if (respCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }

        return (result != null) ? result.toString() : null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            if (s == null){
                Toast.makeText(context, "CEP não localizado", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                return;
            }

            JSONObject object = new JSONObject(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
}
