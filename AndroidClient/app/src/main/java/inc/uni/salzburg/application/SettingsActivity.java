package inc.uni.salzburg.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import hu.kecsot.ktvaluebar.KTValueBar;
import hu.kecsot.ktvaluebar.KTValueBarParams;
import hu.kecsot.ktvaluebar.valuebar.listener.KTValueBarChangedListener;
import inc.uni.salzburg.R;
import inc.uni.salzburg.database.UserSessionUtilities;
import inc.uni.salzburg.model.UserSession;
import inc.uni.salzburg.services.RestaurantFetchService;

public class SettingsActivity extends AppCompatActivity {


    private static final String LOG_TAG = "SA";
    private int mSeekbarValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar_home_logo);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        final TextView radiusText = findViewById(R.id.radius_text);

        KTValueBar mSeekBar = findViewById(R.id.radius_seekbar);
        UserSession userSession = UserSessionUtilities.getCurrentUserSessionSP(SettingsActivity.this);
        mSeekBar.setParams(new KTValueBarParams.Builder().setActualValue(userSession.getRadius()).build());
        radiusText.setText(String.format("%d km", userSession.getRadius()));

        mSeekBar.setValueBarChangedListener(new KTValueBarChangedListener() {
            @Override
            public void onChanged(int i) {
                mSeekbarValue = i;
                radiusText.setText(String.format("%d km", i));
            }
        });
    }


    @Override
    public void onBackPressed() {
        UserSession userSession = UserSessionUtilities.getCurrentUserSessionSP(SettingsActivity.this);
        userSession.setRadius(mSeekbarValue);
        UserSessionUtilities.updateUserSessionSP(SettingsActivity.this, userSession);
        startService(new Intent(SettingsActivity.this, RestaurantFetchService.class));
        super.onBackPressed();
    }
}
