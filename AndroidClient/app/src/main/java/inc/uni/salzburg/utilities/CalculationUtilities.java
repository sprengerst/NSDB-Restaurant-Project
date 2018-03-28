package inc.uni.salzburg.utilities;

import android.content.Context;
import android.util.Log;

import inc.uni.salzburg.database.UserSessionUtilities;
import inc.uni.salzburg.model.UserSession;

/**
 * Created by Stefan Sprenger on 28.03.2018.
 */

public class CalculationUtilities {
    public static double calculateDistance(double lat1, double lon1, Context context) {

        UserSession currentUser = UserSessionUtilities.getCurrentUserSessionSP(context);

        double lat2 = currentUser.getLatitude();
        double lon2 = currentUser.getLongitude();

        Log.d("UIU", "Calculate distance between (lat/long) : " + lat1 + "/" + lon1 + " and " + lat2 + "/" + lon2);

        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1); // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km

        return Math.round(d);
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}
