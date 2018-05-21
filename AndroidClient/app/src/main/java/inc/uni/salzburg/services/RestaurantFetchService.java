package inc.uni.salzburg.services;

/*
 * Downloads Event Suggestions
 */

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import inc.uni.salzburg.R;
import inc.uni.salzburg.database.RestaurantColumns;
import inc.uni.salzburg.database.RestaurantFeedProvider;
import inc.uni.salzburg.database.UserSessionUtilities;
import inc.uni.salzburg.model.Restaurant;
import inc.uni.salzburg.model.UserSession;
import inc.uni.salzburg.utilities.DatabaseUtilities;
import inc.uni.salzburg.utilities.ServerUtilities;

public class RestaurantFetchService extends IntentService {

    final DecimalFormat df = new DecimalFormat("##.#######");

    public RestaurantFetchService() {
        super(RestaurantFetchService.class.getName());
    }

    public RestaurantFetchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // delete old entries
            Context context = getApplicationContext();
            DatabaseUtilities.deleteLocalDatabase(context);

            // delete old suggestion db

            Map<String, String> params = new HashMap<>();

            UserSession userSession = UserSessionUtilities.getCurrentUserSessionSP(context);


            String serverUrl = "http://35.157.39.44/getRestaurantsForLatLngRad";
            params.put("lat", df.format(userSession.getLatitude()).replace(",","."));
            params.put("lng", df.format(userSession.getLongitude()).replace(",","."));
            params.put("rad", String.valueOf(userSession.getRadius()));

            String result = ServerUtilities.post(context, serverUrl, params, -1);

            JSONArray resultJsonObject = new JSONArray(result);

            System.out.println("Complete Restaurant Object: " + resultJsonObject);


            ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

            for (int i = 0; i < resultJsonObject.length(); i++) {
                JSONObject singleRes = (JSONObject) resultJsonObject.get(i);

                final String id = singleRes.getString("_id");
                final String name = singleRes.getString("name");
                final String address = singleRes.getString("vicinity");
                final JSONObject location = singleRes.getJSONObject("geometry").getJSONObject("location");
                final double lat = location.getDouble("lat");
                final double lng = location.getDouble("lng");

                String imageUrl;

                imageUrl = singleRes.getString("icon");
                if(singleRes.has("photos")){
                    JSONArray photos = singleRes.getJSONArray("photos");
                    if(photos.length() > 0){
                        JSONObject photo = (JSONObject)photos.get(0);
                        final String photoRef = photo.getString("photo_reference");
                        imageUrl = "http://maps.googleapis.com/maps/api/place/photo?" +
                                "maxwidth=400" +
                                "&photoreference=" +photoRef +
                                "&key=AIzaSyB9gHbsVRp0nqRS0xMmoeSjL6NkO7zTDjw";
                    }
                }

                Restaurant singleRestaurant = new Restaurant(
                        id,
                        name,
                        imageUrl,
                        lat,
                        lng,
                        address);

                batchOperations.add(buildAddUpdateRestaurantSuggestionOperation(singleRestaurant));

            }

            // add new suggestions
            context.getContentResolver().applyBatch(RestaurantFeedProvider.AUTHORITY, batchOperations);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private ContentProviderOperation buildAddUpdateRestaurantSuggestionOperation(Restaurant restaurant) {
        ContentProviderOperation.Builder builder;

        builder = ContentProviderOperation.newInsert(
                RestaurantFeedProvider.RESTAURANT_FEED.CONTENT_URI);


        try {

            builder.withValue(RestaurantColumns.RESTAURANT_ID, restaurant.getId());
            builder.withValue(RestaurantColumns.RESTAURANT_NAME, restaurant.getName());
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

