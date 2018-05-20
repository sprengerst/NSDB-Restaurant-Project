package inc.uni.salzburg.utilities;

import android.content.Context;

import inc.uni.salzburg.database.RestaurantFeedProvider;

public class DatabaseUtilities {

    public static void deleteLocalDatabase(Context context) {
        context.getContentResolver().delete(RestaurantFeedProvider.RESTAURANT_FEED.CONTENT_URI, null, null);
    }

}
