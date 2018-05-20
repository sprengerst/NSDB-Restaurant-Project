package inc.uni.salzburg.application;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;

public class StartupApplication extends Application {

    private static final String LOG_TAG = "SA";

    private GeoDataClient geoDataClient;

    public StartupApplication() {
        geoDataClient = null;
        Log.i(LOG_TAG, "Application restart fired");
    }

    public void setClient(GeoDataClient client){
        geoDataClient = client;
    }

    public GeoDataClient getClient(){
        return geoDataClient;
    }
}