package aiec.br.ehc.task;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

import aiec.br.ehc.model.Place;

/**
 * Created by gilmar on 01/06/17.
 */

public class CheckPlaceAvailable extends AsyncTask <Place, Boolean, Boolean> {
    private final TextView view;
    private Place place;

    public CheckPlaceAvailable(TextView view) {
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(Place... places) {
        this.place = places[0];
        try {
            URL url = new URL(place.getProtocol().concat("://").concat(place.getHost()));
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean isAvailable) {
        Drawable drawable = view.getContext().getDrawable(android.R.drawable.presence_online);
        drawable.setColorFilter(Color.rgb(205, 85, 85), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, 40, 40);
        String state = "offline";
        if (isAvailable) {
            drawable.setColorFilter(Color.rgb(122, 172, 65), PorterDuff.Mode.SRC_ATOP);
            state = "online";
        }
        view.setCompoundDrawables(drawable, null, null, null);
        view.setText(state);
    }
}
