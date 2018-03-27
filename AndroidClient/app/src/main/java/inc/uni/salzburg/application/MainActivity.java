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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import inc.uni.salzburg.R;
import inc.uni.salzburg.database.RestaurantFeedProvider;
import inc.uni.salzburg.services.RestaurantFetchService;
import inc.uni.salzburg.utilities.Statics;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int RESTAURANT_FEED_LOADER = 1223;

    private RestaurantSwipePagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == RESTAURANT_FEED_LOADER) {
        }
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

            if(data.getCount() != 0){
                mViewPager.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);
            }else {
                mViewPager.setVisibility(View.GONE);
                mProgressView.setVisibility(View.VISIBLE);
            }

        }
    }
}
