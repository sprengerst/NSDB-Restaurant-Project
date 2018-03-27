package inc.uni.salzburg.model;

import android.database.Cursor;

import inc.uni.salzburg.database.RestaurantColumns;
import inc.uni.salzburg.model.Restaurant;

/**
 * Created by Stefan Sprenger on 27.03.2018.
 */

public class ModelUtils {


    public static Restaurant buildRestaurantFromCursor(Cursor cursor){
        return new Restaurant(
                cursor.getInt(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_ID)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_NAME)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_IMAGE_URL)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_RATING)),
                cursor.getDouble(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_LAT)),
                cursor.getDouble(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_LON)),
                cursor.getString(cursor.getColumnIndex(RestaurantColumns.RESTAURANT_ADDRESS))
                );
    }
}
