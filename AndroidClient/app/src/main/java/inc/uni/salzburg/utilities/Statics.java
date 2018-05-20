package inc.uni.salzburg.utilities;

import inc.uni.salzburg.database.RestaurantColumns;

/**
 * Created by Stefan Sprenger on 27.03.2018.
 */

public class Statics {

    // Database Fields
    public static final String[] restaurantColumns = new String[]{
            RestaurantColumns._ID,
            RestaurantColumns.RESTAURANT_ID,
            RestaurantColumns.RESTAURANT_NAME,
            RestaurantColumns.RESTAURANT_IMAGE_URL,
            RestaurantColumns.RESTAURANT_LAT,
            RestaurantColumns.RESTAURANT_LON,
            RestaurantColumns.RESTAURANT_ADDRESS
    };
}
