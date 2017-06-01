package aiec.br.ehc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import aiec.br.ehc.model.Place;

/**
 * Created by gilmar on 01/06/17.
 */

public class CheckPlaceAvailable extends AsyncTask {
    private final Context context;
    private final TextView view;
    private Place place;

    public CheckPlaceAvailable(Context context, Place place, TextView view) {
        this.place = place;
        this.context = context;
        this.view = view;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String domain = place.getProtocol().concat("://").concat(place.getHost());
            SocketAddress sockaddr = new InetSocketAddress(domain, place.getPort());
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            return true;
        } catch(IOException e) {
            return false;
        }
    }
}
