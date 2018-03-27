package inc.uni.salzburg.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 *
 * Holds the columns for buddy feed
 *
 */
public class RestaurantColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String RESTAURANT_ID = "restaurant_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String RESTAURANT_NAME="restaurant_name";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String RESTAURANT_IMAGE_URL="restaurant_image_url";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String RESTAURANT_RATING="restaurant_rating";

    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String RESTAURANT_LAT="restaurant_lat";

    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String RESTAURANT_LON="restaurant_lon";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String RESTAURANT_ADDRESS="restaurant_address";
}
