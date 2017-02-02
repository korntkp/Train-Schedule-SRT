package io.github.shredktp.trainschedulesrt.select_station;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.claudiodegio.msv.BaseMaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.StationDataSource;
import io.github.shredktp.trainschedulesrt.data.StationDataSourceImpl;
import io.github.shredktp.trainschedulesrt.model.Station;

public class SelectStationActivity extends AppCompatActivity implements View.OnClickListener, OnSearchViewListener {

    public static final int REQUEST_CODE_START_STATION = 12794;
    public static final int REQUEST_CODE_END_STATION = 12795;
    public static final String EXTRA_KEY_REQUEST_CODE = "request_c";
    public static final String INTENT_EXTRA_KEY_STATION = "station";
    private static final String TAG = "SelectStaAct";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    private DividerItemDecoration dividerItemDecoration;

//    Button btnStation;
    int req;
    ArrayList<Station> stationArrayList;
    BaseMaterialSearchView baseMaterialSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        setupToolbar();
        getExtraFromIntent();
        setupView();
        setupStationFromDb();
        setupRecyclerView();
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
        baseMaterialSearchView = (BaseMaterialSearchView) findViewById(R.id.select_station_search_view);
        baseMaterialSearchView.setOnSearchViewListener(this);
        baseMaterialSearchView.setVisibility(View.VISIBLE);

//        btnStation = (Button) findViewById(R.id.btn_station);
//        btnStation.setOnClickListener(this);
    }

    private void setupStationFromDb() {
        StationDataSource stationDataSource = new StationDataSourceImpl(getApplicationContext());
        stationArrayList = stationDataSource.getAllStation();
        Log.d(TAG, "setupStationFromDb: " + stationArrayList.size());
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_station: {
//                returnStationResult();
//                break;
//            }
//        }
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.select_station_recycler_view);

        // use a linear layout manager
        layoutManagerRecyclerView = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerRecyclerView);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // specify an adapter (see also next example)
        adapterRecyclerView = new SelectStationAdapter(stationArrayList, SelectStationActivity.this, req);
        recyclerView.setAdapter(adapterRecyclerView);
    }

//    private void returnStationResult(String stationName) {
//        Intent resultIntent = new Intent();
//        if (req == REQUEST_CODE_START_STATION) {
//            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, stationName);
//        } else if (req == REQUEST_CODE_END_STATION) {
//            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, stationName);
//        } else {
//            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, "-");
//        }
//        setResult(Activity.RESULT_OK, resultIntent);
//        finish();
//    }

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
        queryStation(s);
        return true;
    }

    @Override
    public void onQueryTextChange(String s) {
        Log.d(TAG, "onQueryTextChange: " + s);
        queryStation(s);
    }

    private void queryStation(String pieceOfStation) {
        StationDataSource stationDataSource = new StationDataSourceImpl(getApplicationContext());
        stationArrayList = stationDataSource.searchStation(pieceOfStation);
        adapterRecyclerView = new SelectStationAdapter(stationArrayList, SelectStationActivity.this, req);
        adapterRecyclerView.notifyDataSetChanged();
        recyclerView.setAdapter(adapterRecyclerView);
    }
}
