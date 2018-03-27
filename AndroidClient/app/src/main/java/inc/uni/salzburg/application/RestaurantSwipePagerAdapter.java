package inc.uni.salzburg.application;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import inc.uni.salzburg.model.ModelUtils;
import inc.uni.salzburg.model.Restaurant;

public class RestaurantSwipePagerAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private Cursor cursor;

    public RestaurantSwipePagerAdapter(FragmentManager fm, Context context, Cursor cursor) {
        super(fm);
        mContext = context;
        this.cursor = cursor;
    }

    public Fragment getItem(int position) {
        if(cursor.moveToPosition(position)) {
            Restaurant restaurant = ModelUtils.buildRestaurantFromCursor(cursor);

            Log.d("RSPA"," Restaurant: "+restaurant);
            return RestaurantViewItemFragment.newInstance(restaurant);
        }else{
            return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return (cursor == null) ? 0 : cursor.getCount()+1;
    }

    public void setCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
