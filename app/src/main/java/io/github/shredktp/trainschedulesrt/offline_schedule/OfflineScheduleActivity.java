package io.github.shredktp.trainschedulesrt.offline_schedule;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.data.TrainSchedule;
import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationLocalDataSource;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;
import io.github.shredktp.trainschedulesrt.show_schedule.ScheduleRecyclerViewAdapter;

public class OfflineScheduleActivity extends AppCompatActivity {

    private static final String TOOLBAR_TITLE = "Train Schedule";
    private static final String EXTRA_START_STATION = "startStation";
    private static final String EXTRA_END_STATION = "endStation";
    private static final String TAG = "OfflineAct";

    private Toolbar toolbar;

    private RecyclerView recyclerViewOfflineSchedule;
    private RecyclerView.Adapter adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    private DividerItemDecoration mDividerItemDecoration;

    private TextView tvTitleStartStation, tvTitleEndStation;

    private ArrayList<TrainSchedule> trainScheduleArrayList;

    MenuItem menuItemBookmark, menuItemBookmarked;
    private boolean isBookmarked;
    private String startStation = "";
    private String endStation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_schedule);

        startStation = getIntent().getStringExtra(EXTRA_START_STATION);
        endStation = getIntent().getStringExtra(EXTRA_END_STATION);

        setupView(startStation, endStation);
        setupToolbar();
        setupData(startStation, endStation);
        setupRecyclerView();
        isBookmarked = isSeeItFirstStation(startStation, endStation);
    }

    private void setupView(String startStation, String endStation) {
        toolbar = (Toolbar) findViewById(R.id.offline_schedule_toolbar);
        tvTitleStartStation = (TextView) findViewById(R.id.tv_title_start_station);
        tvTitleEndStation = (TextView) findViewById(R.id.tv_title_end_station);
        recyclerViewOfflineSchedule = (RecyclerView) findViewById(R.id.recycler_view_offline_schedule);

        tvTitleStartStation.setText(startStation);
        tvTitleEndStation.setText(endStation);
    }

    private void setupToolbar() {
        toolbar.setTitle(TOOLBAR_TITLE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupData(String startStation, String endStation) {
        trainScheduleArrayList = TrainScheduleLocalDataSource.getInstance(getApplicationContext()).getTrainScheduleByStation(startStation, endStation);
    }

    private void setupRecyclerView() {
        layoutManagerRecyclerView = new LinearLayoutManager(this);
        recyclerViewOfflineSchedule.setLayoutManager(layoutManagerRecyclerView);
        mDividerItemDecoration = new DividerItemDecoration(recyclerViewOfflineSchedule.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewOfflineSchedule.addItemDecoration(mDividerItemDecoration);

        adapterRecyclerView = new ScheduleRecyclerViewAdapter(trainScheduleArrayList);
        recyclerViewOfflineSchedule.setAdapter(adapterRecyclerView);
    }

    private boolean isSeeItFirstStation(String startStation, String endStation) {
        PairStation pairStation;
        try {
            pairStation = PairStationLocalDataSource
                    .getInstance(Contextor.getInstance().getContext()).getSeeFirstPairStation();
        } catch (NullPointerException e) {
            Log.i(TAG, "isSeeItFirstStation: No Star Station");
            return false;
        }

        Log.i(TAG, "getSeeFirstPairStation Start: " + pairStation.getStartStation());
        Log.i(TAG, "getSeeFirstPairStation End: " + pairStation.getEndStation());

        return startStation.equals(pairStation.getStartStation()) && endStation.equals(pairStation.getEndStation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.see_schedule_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark: {
                PairStation pairStation = new PairStation(startStation, endStation, 0, 1, 0);
                PairStationLocalDataSource.getInstance(Contextor.getInstance().getContext())
                        .clearThenUpdateSeeItFirst(pairStation);
                Snackbar.make(findViewById(R.id.main_coordinator), "This schedule is bookmarked", Snackbar.LENGTH_SHORT).show();
                item.setVisible(false);
                menuItemBookmarked.setVisible(true);
                return true;
            }
            case R.id.action_bookmarked: {
                // TODO: 04-Apr-17 Update See it first DataSource
                PairStationLocalDataSource.getInstance(Contextor.getInstance().getContext())
                        .updateSeeItFirst(startStation, endStation, 0);
                Snackbar.make(findViewById(R.id.main_coordinator), "This schedule is removed from bookmarked", Snackbar.LENGTH_SHORT).show();
                item.setVisible(false);
                menuItemBookmark.setVisible(true);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "onPrepareOptionsMenu: Start");
        menuItemBookmark = menu.findItem(R.id.action_bookmark);
        menuItemBookmarked = menu.findItem(R.id.action_bookmarked);

        if (isBookmarked) {
            menuItemBookmark.setVisible(false);
            menuItemBookmarked.setVisible(true);
        } else {
            menuItemBookmarked.setVisible(false);
            menuItemBookmark.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }
}
