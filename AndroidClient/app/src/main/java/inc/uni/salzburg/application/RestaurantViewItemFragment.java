package inc.uni.salzburg.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import inc.uni.salzburg.R;
import inc.uni.salzburg.model.Restaurant;
import inc.uni.salzburg.utilities.CalculationUtilities;


public class RestaurantViewItemFragment extends Fragment {

    private static final String ARG_RESTAURANT = "arg_restaurant";

    public RestaurantViewItemFragment() {
    }

    public static RestaurantViewItemFragment newInstance(Restaurant restaurant) {
        RestaurantViewItemFragment fragment = new RestaurantViewItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESTAURANT, restaurant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null && getArguments().getParcelable(ARG_RESTAURANT) != null) {
            final Restaurant restaurant = getArguments().getParcelable(ARG_RESTAURANT);
            View rootView = inflater.inflate(R.layout.fragment_restaurant_view_item, container, false);


            if (restaurant != null) {


                TextView restaurantName = rootView.findViewById(R.id.restaurant_name);
                restaurantName.setText(restaurant.getName());

                TextView restaurantAddress = rootView.findViewById(R.id.restaurant_address);
                restaurantAddress.setText(restaurant.getAddress());

                ImageView restaurantImage = rootView.findViewById(R.id.restaurant_image);
                Glide.with(getContext())
                        .load(restaurant.getImageUrl())
                        .apply(new RequestOptions().centerCrop())
                        .into(restaurantImage);


//                ServerUtilities.getPlacePhotoByID(restaurant.getId(), getContext(),restaurantImage);

                final String distanceString = String.format(getString(R.string.distance_text), CalculationUtilities.calculateDistance(restaurant.getLatitude(), restaurant.getLongitude(), getContext()));

                TextView restaurantDistance = rootView.findViewById(R.id.restaurant_distance);
                restaurantDistance.setText(distanceString);

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + restaurant.getLatitude() + ">,<" + restaurant.getLongitude() + ">?q=<" + restaurant.getLatitude() + ">,<" + restaurant.getLongitude() + ">(" + restaurant.getName() + ")"));
                        startActivity(intent);
                    }
                });


            }
            return rootView;
        } else {
            return null;
        }
    }
}
