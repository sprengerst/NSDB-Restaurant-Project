package inc.uni.salzburg.services;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import inc.uni.salzburg.R;
import inc.uni.salzburg.database.UserSessionUtilities;
import inc.uni.salzburg.model.UserSession;
import inc.uni.salzburg.utilities.ErrorHandlingUtilities;


/**
 * Translates lat/long via google servers to human readable address
 */
public class GeoCodingTask extends AsyncTask<Void, Void, String> {
    private final String LOG_TAG = GeoCodingTask.class.getSimpleName();
    private final Context mContext;

    private final float latitude;
    private final float longitude;

    private TextView insertTextView;

    public GeoCodingTask(Context context, TextView insertTextView, float latitude, float longitude) {
        this.mContext = context;
        this.insertTextView = insertTextView;
        this.latitude = latitude;
        this.longitude = longitude;

        if (insertTextView != null) {
            insertTextView.setText("Loading");
        }
    }

    @Override
    protected String doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String locationJSONStr;

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("maps.googleapis.com")
                    .appendPath("maps")
                    .appendPath("api")
                    .appendPath("geocode")
                    .appendPath("json")
                    .appendQueryParameter("latlng", latitude + "," + longitude)
                    .appendQueryParameter("result_type", "political")
                    .appendQueryParameter("language", "de")
                    .appendQueryParameter("key", "AIzaSyARIehNPlSxMWkosZ8BvvVc9hyeBiYc4aM");



            URL url = new URL(builder.build().toString());

            Log.d("GEOCODING TAG: ", url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            locationJSONStr = buffer.toString();
            Log.e("TAG: ", locationJSONStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOEXCEPTION", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "IOEXCEPTION", e);
                }
            }
        }

        try {
            return fetchLocationData(locationJSONStr);
        } catch (Exception e) {
            Log.e(LOG_TAG, "EXCEPTION", e);
            e.printStackTrace();
        }

        return null;

    }

    private String fetchLocationData(String locationJSONStr) throws JSONException {

        final String GEMEINDESTRING = "Gemeinde ";

        final String LOCATIONLIST = "results";
        final String COMPONENTSLIST = "address_components";
        final String LONGNAME = "long_name";
        final String TYPES = "types";
        final String COUNTRY = "country";
        final String ADM_LEVEL_1 = "administrative_area_level_1";
        final String ADM_LEVEL_2 = "administrative_area_level_2";
        final String ADM_LEVEL_3 = "administrative_area_level_3";
        final String LOCALITY = "locality";
        final String SUBLOCALITY = "sublocality_level_1";
        JSONObject pageJSON = new JSONObject(locationJSONStr);
        JSONArray resultContainer = pageJSON.getJSONArray(LOCATIONLIST);
        JSONArray componentContainer = resultContainer.getJSONObject(0).getJSONArray(COMPONENTSLIST);

        for (int i = 0; i < componentContainer.length(); i++) {

            JSONObject singleAddressComponent = componentContainer.getJSONObject(i);

            String longName = singleAddressComponent.getString(LONGNAME);
            JSONArray locationTypes = singleAddressComponent.getJSONArray(TYPES);

            for (int j = 0; j < locationTypes.length(); j++) {
                String foundType = locationTypes.getString(j);
                switch (foundType) {
                    case SUBLOCALITY:
                    case LOCALITY:
                    case ADM_LEVEL_3:
                    case ADM_LEVEL_2:
                    case ADM_LEVEL_1:
                    case COUNTRY:
                        if (longName.contains(GEMEINDESTRING)) {
                            longName = longName.substring(longName.indexOf(GEMEINDESTRING) + GEMEINDESTRING.length(), longName.length());
                        }
                        return longName;
                }
            }
        }

        return null;
    }

    protected void onPostExecute(String result) {
        if (result != null && !result.equals("")) {

            String locationNameLong = result.substring(result.lastIndexOf(",") + 1);

            if (insertTextView != null) {
                insertTextView.setText(locationNameLong);
            }

            // Update User Session
            UserSession userSession = UserSessionUtilities.getCurrentUserSessionSP(mContext);
            userSession.setGeoResolution(locationNameLong);
            userSession.setLatitude(latitude);
            userSession.setLongitude(longitude);
            UserSessionUtilities.updateUserSessionSP(mContext, userSession);

        } else {
            if (ErrorHandlingUtilities.isNetworkAvailableWithToast(mContext)) {
                if (insertTextView != null) {
                    ErrorHandlingUtilities.showToast("ERROR", mContext);
                }
            }

            if (insertTextView != null) {
                insertTextView.setText("ERROR");
            }
        }
    }
}
