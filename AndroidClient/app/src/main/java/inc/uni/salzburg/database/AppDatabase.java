package inc.uni.salzburg.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Holds the database main functionality
 */

@Database(
        className = AppDatabase.CLASS, fileName = AppDatabase.NAME,
        version = AppDatabase.VERSION,
        packageName = "inc.uni.salzburg.database.provider"
)
public class AppDatabase {

    public static final String NAME = "NSDB-Restaurant.db";
    public static final String CLASS = "AppDatabase";
    public static final int VERSION = 1;

    private AppDatabase() {
    }

    @Table(RestaurantColumns.class)
    public static final String RESTAURANT_FEED ="restaurantFeed";
}
