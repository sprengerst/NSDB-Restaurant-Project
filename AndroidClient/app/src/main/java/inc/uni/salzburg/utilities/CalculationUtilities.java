package inc.uni.salzburg.utilities;

import android.content.Context;
import android.util.Log;

import inc.uni.salzburg.database.UserSessionUtilities;
import inc.uni.salzburg.model.UserSession;

/**
 * Created by Stefan Sprenger on 28.03.2018.
 */

public class CalculationUtilities {

    public static int calculateDistance(double lat1, double lng1,  Context context) {


        UserSession currentUser = UserSessionUtilities.getCurrentUserSessionSP(context);

        double lat2 = currentUser.getLatitude();
        double lng2 = currentUser.getLongitude();

        Log.d("UIU", "Calculate distance between (lat/long) : " + lat1 + "/" + lng1 + " and " + lat2 + "/" + lng2);

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (int) (earthRadius * c);
    }

}
