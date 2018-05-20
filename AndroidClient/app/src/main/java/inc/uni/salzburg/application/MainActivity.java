package inc.uni.salzburg.application;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import inc.uni.salzburg.R;
import inc.uni.salzburg.database.RestaurantFeedProvider;
import inc.uni.salzburg.database.UserSessionUtilities;
import inc.uni.salzburg.model.UserSession;
import inc.uni.salzburg.services.GeoCodingTask;
import inc.uni.salzburg.services.RestaurantFetchService;
import inc.uni.salzburg.utilities.ErrorHandlingUtilities;
import inc.uni.salzburg.utilities.Statics;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int RESTAURANT_FEED_LOADER = 1223;

    private RestaurantSwipePagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View mProgressView;
    private ActionMenuView leftToolbarMenu;
    private TextView mLocationTextView;

    private static final int REQUEST_LOCATION_MAIN = 9874;

    private static final String LOG_TAG = "MA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSession userSession = UserSessionUtilities.getCurrentUserSessionSP(MainActivity.this);
        if (userSession == null) {
            userSession = new UserSession(null, 47.811195, 13.033229, 2);
            UserSessionUtilities.updateUserSessionSP(MainActivity.this, userSession);
        }

        initGoogleServices();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_home_logo);
        leftToolbarMenu = toolbar.findViewById(R.id.action_menu_toolbar);
        leftToolbarMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        mSectionsPagerAdapter = new RestaurantSwipePagerAdapter(getSupportFragmentManager(), MainActivity.this, null);

        mViewPager = findViewById(R.id.contentViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mProgressView = findViewById(R.id.loadingView);

        mViewPager.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        getSupportLoaderManager().initLoader(RESTAURANT_FEED_LOADER, null, this);

        startService(new Intent(MainActivity.this, RestaurantFetchService.class));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.size() == 0) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);


            // if left toolbar already initialized inflate
            if (leftToolbarMenu.getMenu().size() == 0) {
                getMenuInflater().inflate(R.menu.menu_home_advanced_toolbar, leftToolbarMenu.getMenu());

                final MenuItem locationMenu = leftToolbarMenu.getMenu().findItem(R.id.location_menu_item);
                final LinearLayout rootViewLocation = (LinearLayout) locationMenu.getActionView();
                mLocationTextView = rootViewLocation.findViewById(R.id.current_location_menu);
                rootViewLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("MA", "Updating Location");
                        try {
                            if (ErrorHandlingUtilities.isNetworkAvailableWithToast(MainActivity.this)) {
                                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                                Intent intent = intentBuilder.build(MainActivity.this);
                                startActivityForResult(intent, REQUEST_LOCATION_MAIN);
                            }
                        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                            Log.e("Main", "Error fetching Location " + e.toString());
                            e.printStackTrace();
                        }
                    }
                });

                updateUserPositionToolbar();

            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUserPositionToolbar() {
        final UserSession userSession = UserSessionUtilities.getCurrentUserSessionSP(MainActivity.this);
        Log.d("MA", "Update user postion with: " + userSession.getGeoResolution());
        if (userSession.getGeoResolution() == null) {
            if (userSession.getLatitude() != -1 && userSession.getLongitude() != -1) {
                new GeoCodingTask(this, mLocationTextView, userSession.getLatitude(), userSession.getLongitude()).execute();
            }
        } else {
            updateUserLocationResolution(userSession.getGeoResolution());
        }
    }

    public void updateUserLocationResolution(String resolution) {
        if (mLocationTextView != null) {
            mLocationTextView.setText(resolution);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == RESTAURANT_FEED_LOADER) {
            return new CursorLoader(MainActivity.this, RestaurantFeedProvider.RESTAURANT_FEED.CONTENT_URI,
                    Statics.restaurantColumns,
                    null,
                    null,
                    null);
        }

        return null;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == RESTAURANT_FEED_LOADER) {
            mSectionsPagerAdapter.setCursor(data);

            if (data.getCount() != 0) {
                mViewPager.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);
            } else {
                mViewPager.setVisibility(View.GONE);
                mProgressView.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }

        if (requestCode == REQUEST_LOCATION_MAIN) {
            if (resultCode == RESULT_OK) {
                if ((data != null) && (data.getExtras() != null)) {
                    Place place = PlacePicker.getPlace(MainActivity.this, data);
                    Log.d("MainActivity", "LOCAL: " + place.getLocale() + " Name: " + place.getName());
                    new GeoCodingTask(this, mLocationTextView, (float) place.getLatLng().latitude, (float) place.getLatLng().longitude).execute();
                    startService(new Intent(MainActivity.this, RestaurantFetchService.class));
                    getSupportLoaderManager().restartLoader(RESTAURANT_FEED_LOADER, null, this);
                }
            }
        }

    }


    private void initGoogleServices() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).
                addApi(Places.GEO_DATA_API).
                addApi(Places.PLACE_DETECTION_API).
                enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(LOG_TAG, "Google connection failed");
                    }
                }).
                build();

        StartupApplication startupApplication = ((StartupApplication) getApplicationContext());
        startupApplication.setClient(Places.getGeoDataClient(this));
    }


}
