package io.github.shredktp.trainschedulesrt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.claudiodegio.msv.BaseMaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;

public class SearchStationActivity extends AppCompatActivity implements View.OnClickListener, OnSearchViewListener {

    public static final int REQUEST_CODE_START_STATION = 12794;
    public static final int REQUEST_CODE_END_STATION = 12795;
    public static final String EXTRA_KEY_REQUEST_CODE = "request_c";
    public static final String INTENT_EXTRA_KEY_STATION = "station";
    private static final String TAG = "SearchStaAct";

    Button btnStation;
    int req;
    BaseMaterialSearchView baseMaterialSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        setupToolbar();
        getExtraFromIntent();
        setupView();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_station_toolbar);
        setSupportActionBar(toolbar);
    }

    private void getExtraFromIntent() {
        Intent intent = getIntent();
        req = intent.getIntExtra(EXTRA_KEY_REQUEST_CODE, -1);
    }

    private void setupView() {
        baseMaterialSearchView = (BaseMaterialSearchView) findViewById(R.id.search_station_search_view);
        baseMaterialSearchView.setOnSearchViewListener(this);
        baseMaterialSearchView.setVisibility(View.VISIBLE);

        btnStation = (Button) findViewById(R.id.btn_station);
        btnStation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_station: {
                returnStationResult();
                break;
            }
        }
    }

    private void returnStationResult() {
        Intent resultIntent = new Intent();
        if (req == REQUEST_CODE_START_STATION) {
            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, "กรุงเทพ");
        } else if (req == REQUEST_CODE_END_STATION) {
            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, "อยุธยา");
        } else {
            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, "-");
        }
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        baseMaterialSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(TAG, "onQueryTextSubmit: " + s);
        Toast.makeText(this, "Searching: " + s, Toast.LENGTH_SHORT).show();
        queryStation();
        return true;
    }

    @Override
    public void onQueryTextChange(String s) {
        Log.d(TAG, "onQueryTextChange: " + s);
        queryStation();
    }

    private void queryStation() {
        // TODO: 31-Jan-17 List View
    }
}
