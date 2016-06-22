package br.com.treinaweb.candidatostreinaweb.common;

/**
 * Created by Edu on 02/02/2016.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {

    public static InputStream OpenHttpConnection(String urlString, Context context) throws IOException
    {
        InputStream is = null;
        int response = -1;

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("N�o � uma conex�o HTTP");
            try{
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    is = httpConn.getInputStream();
                }
            }
            catch (Exception ex)
            {
                Log.i("HttpConnection", ex.getLocalizedMessage());
                throw new IOException("Erro ao se conectar");
            }
        }

        return is;
    }
}
