package aiec.br.ehc.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import aiec.br.ehc.R;
import aiec.br.ehc.adapter.IResourceView;
import aiec.br.ehc.model.Place;
import aiec.br.ehc.model.Resource;

/**
 * Responsável por fazer as requisições ao servidor a fim de prover a comunicação entre o aplicativo
 * e os recursos providos pelo mesmo.
 */
public class ResourceRequestTask extends AsyncTask<Resource, Void, String> {
    private final Context context;
    private IResourceView resourceView;
    private ProgressDialog dialog;
    private HttpURLConnection conn = null;
    private int respCode;
    private String result;
    private Resource resource;
    private String error;
    private URL url;
    private boolean use_authorization = false;
    private String flagInfo = "";
    private String queryString;
    private Place place;
    private boolean showPogressDialog = true;
    private boolean showFailConnectionNotification = true;

    public ResourceRequestTask(IResourceView resourceView) {
        this.resourceView = resourceView;
        this.context = resourceView.getContext();
    }

    @Override
    protected String doInBackground(Resource... params) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.resource = params[0];
        this.place = resource.getPlace(context);
        URL url = resource.getRequestURL(context);
        this.queryString = resource.getParamsQueryString(context);
        if (place.isSendTokenByUrl() && place.isAuthorizationByToken()) {
            String glue = queryString.isEmpty() ? "" : "&";
            String token = place.getAccessToken();
            this.queryString.concat(glue).concat(place.getTokenFlag()).concat("=").concat(token);
        }
        respCode = -1;
        conn = this.getConnection(resource.getMethod(), url, queryString);
        if (conn != null) {
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            this.setCredentials();
            try {
                conn.connect();
                respCode = conn.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                result = response.toString();

            } catch (IOException e) {
                this.error = context.getString(R.string.http_request_command_fail);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            }
        }

        return (result != null) ? result : null;
    }

    private HttpURLConnection getConnection(String method, URL url, String queryString)
    {
        if (method.equalsIgnoreCase("post")) {
            return this.sendPost(url, queryString);
        }

        return this.sendGet(url, queryString);
    }

    /**
     * Define as credenciais (token ou login) para uso na conexão
     */
    private void setCredentials()
    {
        // autenticação por login e senha
        String credentials = place.getUserCredentials();
        if (place.isAuthorizationByCredentials() && !TextUtils.isEmpty(credentials)) {
            String credentialEncoded = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
            String authorization = place.getCredentialFlag().concat(" ") + credentialEncoded;
            conn.setRequestProperty("Authorization", authorization);
            this.use_authorization = true;
            this.flagInfo = "Authorization: ".concat(place.getCredentialFlag().concat(" <token>"));
            return;
        }

        // autenticação por token
        String token = place.getAccessToken();
        if (place.isAuthorizationByToken() && !TextUtils.isEmpty(token) && place.isSendTokenByHeaders()) {
            this.use_authorization = true;
            token = place.getTokenFlag().concat(" ").concat(token);
            conn.setRequestProperty("Authorization", token);
            this.flagInfo = "Authorization: ".concat(place.getTokenFlag().concat(" <access key>"));
        }
    }

    /**
     * Envia dados via POST
     * @param url   URL base
     * @param queryString   Parâmetros
     *
     * @return Instância de conexão http
     */
    private HttpURLConnection sendPost(URL url, String queryString)
    {
        try {
            this.url = url;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(queryString);
            writer.flush();
            writer.close();
            os.close();
            return conn;
        } catch (ProtocolException e) {
            this.error = context.getString(R.string.error_protocol_send);
        } catch (UnsupportedEncodingException e) {
            this.error = context.getString(R.string.error_data_encoding);
        } catch (IOException e) {
            this.error = context.getString(R.string.error_io);
        }

        return null;
    }

    /**
     * Facilita a criação deste objeto
     *
     * @param resourceView view
     * @return self
     */
    public static ResourceRequestTask createFrom(IResourceView resourceView) {
        return new ResourceRequestTask(resourceView);
    }

    /**
     * Envia dados via POST
     * @param url   URL base
     * @param queryString   Parâmetros
     *
     * @return Instância de conexão http
     */
    private HttpURLConnection sendGet(URL url, String queryString)
    {
        try {
            this.url = new URL(url.toString().concat("?").concat(queryString));
            HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (ProtocolException e) {
            this.error = context.getString(R.string.error_protocol_send);
        } catch (UnsupportedEncodingException e) {
            this.error = context.getString(R.string.error_data_encoding);
        } catch (IOException e) {
            this.error = context.getString(R.string.error_io);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        if (showPogressDialog) {
            dialog = new ProgressDialog(context, R.style.StyledDialog);
            dialog.setMessage(context.getString(R.string.wait));
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        resourceView.setRequestResponse(this.result);
        if (respCode != HttpURLConnection.HTTP_OK && this.showFailConnectionNotification) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.StyledDialog);
            dialog.setTitle(this.error);

            String auth_mode = "none";
            if (this.use_authorization) {
                auth_mode = place.isAuthorizationByCredentials() ?
                        "Login" : "Token";
            }

            String qs = queryString;
            if (!TextUtils.isEmpty(place.getAccessToken())) {
                qs = queryString.replace(place.getAccessToken(), " <access key>");
            }

            String message = "Method: "
                    .concat(resource.getMethod()).concat("\n")
                    .concat("URL: ")
                    .concat(resource.getRequestURL(context).toString().concat("\n"))
                    .concat("Parameters: ")
                    .concat(qs).concat("\n")
                    .concat("Authentication mode: ")
                    .concat(auth_mode).concat("\n")
                    .concat(flagInfo);
            if (this.result !=null) {
                message = result;
            }
            dialog.setView(getDialogView(message));
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            dialog.setCancelable(true);
            dialog.show();
        } else {
            resourceView.applyEffects(resource.getState());
            resource.save(context);
        }

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private View getDialogView(String message)
    {
        TextView tw = new TextView(context);
        tw.setMaxLines(10);
        tw.setLineSpacing(2, 2);
        tw.setPadding(75,25,25,25);
        ViewGroup.LayoutParams viewGroup = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        tw.setLayoutParams(viewGroup);
        tw.setSingleLine(false);
        tw.setText(message);
        return tw;
    }

    /**
     * Desabilita a exibição da mesagem quando há uma falha de comunicação com o servidor
     */
    public ResourceRequestTask showNotificationOnFailConnection(Boolean show) {
        this.showFailConnectionNotification = show;
        return this;
    }

    /**
     * Desabilita a exibição da mensagem de 'Wait...'
     */
    public ResourceRequestTask showProgressDialog(Boolean show) {
        this.showPogressDialog = show;
        return this;
    }
}
