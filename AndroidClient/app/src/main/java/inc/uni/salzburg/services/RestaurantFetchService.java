package inc.uni.salzburg.services;

/*
 * Downloads Event Suggestions
 */

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import inc.uni.salzburg.R;
import inc.uni.salzburg.database.RestaurantColumns;
import inc.uni.salzburg.database.RestaurantFeedProvider;
import inc.uni.salzburg.model.ModelUtils;
import inc.uni.salzburg.model.Restaurant;
import inc.uni.salzburg.utilities.ServerUtilities;

public class RestaurantFetchService extends IntentService {


    public RestaurantFetchService() {
        super(RestaurantFetchService.class.getName());
    }

    public RestaurantFetchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Context context = getApplicationContext();

            // delete old suggestion db
            context.getContentResolver().delete(RestaurantFeedProvider.RESTAURANT_FEED.CONTENT_URI, null, null);

            // TODO server connection
//            Map<String, String> params = new HashMap<>();
//
//            String serverUrl = "http://blablaTODO/restaurantDiscovery";
//            params.put("parameter_TODO", "TEST");
//
//            String result = ServerUtilities.post(context, serverUrl, params, -1);
//
//            JSONObject resultJsonObject = new JSONObject(result);
//            boolean success = resultJsonObject.getBoolean("success");
//            //TODO: handle could not get restaurant from server
//            if (!success)
//                return;


//            JSONArray restaurantSuggestions = resultJsonObject.getJSONArray("restaurantSuggestions");
//            ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
//
//            for (int i = 0; i < restaurantSuggestions.length(); i++) {
//                JSONObject jsonObject = (JSONObject) restaurantSuggestions.get(i);
//                boolean alreadyInLocalDB = ModelUtils.checkForRestaurantExistentByID(jsonObject.getInt("restaurant_id"));
//                // TODO parse json to restaurant
//                batchOperations.add(buildAddUpdateRestaurantSuggestionOperation(alreadyInLocalDB, jsonObject));
//            }
//
//            // add new suggestions
//            context.getContentResolver().applyBatch(RestaurantFeedProvider.AUTHORITY, batchOperations);


            ArrayList<Restaurant> dummyRestaurantList = new ArrayList<>();

            for (int i = 1; i < 20; i++) {
                dummyRestaurantList.add(new Restaurant(i, "Test Restaurant " + i, context.getString(R.string.dummy_image_url), "4.3", 47.8029039, 12.9862185, "Salzburg Addresse"));
            }

            ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

            for (Restaurant singleRestaurant : dummyRestaurantList) {
                boolean alreadyInLocalDB = ModelUtils.checkForRestaurantExistentByID(singleRestaurant.getId(), this);
                batchOperations.add(buildAddUpdateRestaurantSuggestionOperation(alreadyInLocalDB, singleRestaurant));
            }
            // add new suggestions
            context.getContentResolver().applyBatch(RestaurantFeedProvider.AUTHORITY, batchOperations);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private ContentProviderOperation buildAddUpdateRestaurantSuggestionOperation(boolean alreadyInLocalDB, Restaurant restaurant) {
        ContentProviderOperation.Builder builder;

        if (!alreadyInLocalDB) {
            builder = ContentProviderOperation.newInsert(
                    RestaurantFeedProvider.RESTAURANT_FEED.CONTENT_URI);
        } else {
            builder = ContentProviderOperation.newUpdate(
                    RestaurantFeedProvider.RESTAURANT_FEED.CONTENT_URI);
        }

        try {

            if (alreadyInLocalDB)
                builder.withSelection(RestaurantColumns.RESTAURANT_ID + "= ?", new String[]{"" + restaurant.getId()});

            builder.withValue(RestaurantColumns.RESTAURANT_ID, restaurant.getId());
            builder.withValue(RestaurantColumns.RESTAURANT_NAME, restaurant.getName());
            builder.withValue(RestaurantColumns.RESTAURANT_RATING, restaurant.getRating());
            builder.withValue(RestaurantColumns.RESTAURANT_IMAGE_URL, restaurant.getImageUrl());
            builder.withValue(RestaurantColumns.RESTAURANT_LAT, restaurant.getLatitude());
            builder.withValue(RestaurantColumns.RESTAURANT_LON, restaurant.getLongitude());
            builder.withValue(RestaurantColumns.RESTAURANT_ADDRESS, restaurant.getAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }


}

