package inc.uni.salzburg.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import inc.uni.salzburg.model.UserSession;


public class UserSessionUtilities {

    private static final String USERSESSION_OBJECT_KEY = "USER_ID_SHARED_OBJECT";
    private static final String USERSESSION_SHARED_KEY = "USER_ID_SHARED_KEY";

    public static UserSession getCurrentUserSessionSP(Context context) {
        // Get current user session
        SharedPreferences mPrefs = context.getSharedPreferences(USERSESSION_SHARED_KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(USERSESSION_OBJECT_KEY, "");
        return gson.fromJson(json, UserSession.class);
    }

    public static void updateUserSessionSP(Context context, UserSession achievedUser) {
        // add usersession to sharedprefs
        SharedPreferences mPrefs = context.getSharedPreferences(USERSESSION_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Log.e("USU","Update User Session");
        Gson gson = new Gson();
        String json = gson.toJson(achievedUser);
        Log.e("USU",json);
        prefsEditor.putString(USERSESSION_OBJECT_KEY, json);
        prefsEditor.apply();
    }

    public static void deleteUserSessionSP(Context context) {
        Log.e("USU","Delete User Session");

        SharedPreferences mPrefs = context.getSharedPreferences(USERSESSION_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.remove(USERSESSION_OBJECT_KEY);
        prefsEditor.apply();
    }
}
