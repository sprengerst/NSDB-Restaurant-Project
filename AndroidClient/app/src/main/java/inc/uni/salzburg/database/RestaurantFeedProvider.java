package inc.uni.salzburg.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(authority = RestaurantFeedProvider.AUTHORITY, database = AppDatabase.class,
        packageName = "inc.uni.salzburg.database.provider")

public class RestaurantFeedProvider {
    public static final String AUTHORITY = "inc.uni.salzburg.data."+ AppDatabase.RESTAURANT_FEED;

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String RESTAURANT_FEED = AppDatabase.RESTAURANT_FEED;
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = AppDatabase.RESTAURANT_FEED)
    public static class RESTAURANT_FEED {
        @ContentUri(
                path = Path.RESTAURANT_FEED,
                type = "vnd.android.cursor.dir/"+ AppDatabase.RESTAURANT_FEED
        )
        public static final Uri CONTENT_URI = buildUri(Path.RESTAURANT_FEED);
    }




    @InexactContentUri(
            name = "Restaurant_ID",
            path = Path.RESTAURANT_FEED + "/#",
            type = "vnd.android.cursor.item/" + AppDatabase.RESTAURANT_FEED,
            whereColumn = {RestaurantColumns.RESTAURANT_ID},
            pathSegment = {1}
    )
    public static Uri withRestaurantID(int id) {
        return buildUri(Path.RESTAURANT_FEED, String.valueOf(id));
    }
}
