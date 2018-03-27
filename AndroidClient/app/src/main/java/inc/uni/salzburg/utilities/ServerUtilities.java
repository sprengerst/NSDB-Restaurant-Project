package inc.uni.salzburg.utilities;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Stefan Sprenger on 27.03.2018.
 *
 */

public class ServerUtilities {

    private static final String LOG_TAG = "SU";
    private static final int BACKOFF_MILLI_SECONDS = 2000;

    public static String post(Context context, String serverUrl, final Map<String, String> params, int indexCount) throws IOException {

        int backoff = BACKOFF_MILLI_SECONDS;
        int i = 0;
        while (i != indexCount) {
            Log.d(LOG_TAG, "Attempt #" + i + " to postProcedure");
            try {
                return postProcedure(serverUrl, params);
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(LOG_TAG, "Failed to postProcedure on attempt " + i, e);

                try {
                    Log.d(LOG_TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Log.d(LOG_TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return null;
                }
                // increase backoff additional by 2 seconds
                backoff += BACKOFF_MILLI_SECONDS;
            }

            i++;
        }


        return null;
    }

    private static String postProcedure(String endpoint, Map<String, String> params)
            throws IOException {


        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(LOG_TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // postProcedure the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            conn.disconnect();
            in.close();
            Log.d(LOG_TAG, response.toString());
            return response.toString();

        } catch (Exception e) {
            Log.d(LOG_TAG, e.toString());
            throw e;
        }
    }

}
