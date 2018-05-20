package inc.uni.salzburg.model;

import android.content.Context;
import android.database.Cursor;

import inc.uni.salzburg.database.RestaurantColumns;
import inc.uni.salzburg.database.RestaurantFeedProvider;
import inc.uni.salzburg.model.Restaurant;

/**
 * Created by Stefan Sprenger on 27.03.2018.
 */

public class ModelUtils {


    public static Restaurant buildRestaurantFromCursor(Cursor cursor){
        return new Restaurant(
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_ID)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_NAME)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_IMAGE_URL)),
                cursor.getDouble(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_LAT)),
                cursor.getDouble(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_LON)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_ADDRESS))
                );
    }



    public static boolean checkForRestaurantExistentByID(int restaurantID, Context context) {
        Cursor cursor = context.getContentResolver().query(RestaurantFeedProvider.withRestaurantID(restaurantID),
                new String[]{RestaurantColumns.RESTAURANT_ID},
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.getCount() >= 1) {
                cursor.moveToFirst();
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
