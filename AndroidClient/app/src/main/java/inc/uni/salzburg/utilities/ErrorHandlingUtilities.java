package inc.uni.salzburg.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * This class holds utilities for error handling
 */

public class ErrorHandlingUtilities {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startActivityIfNetwork(Context context, Intent intent) {
        if (!isNetworkAvailable(context)) {
            showToast("This is not possible without internet connection. Please check your network!", context);
        } else {
            context.startActivity(intent);
        }
    }

    public static boolean isNetworkAvailableWithToast(Context context) {
        if (!isNetworkAvailable(context)) {
            showToast("This is not possible without internet connection. Please check your network!", context);
        }
        return isNetworkAvailable(context);
    }

    public static void showToast(String toastText, Context context) {
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
    }

}
