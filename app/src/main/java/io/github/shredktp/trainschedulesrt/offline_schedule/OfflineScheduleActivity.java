package io.github.shredktp.trainschedulesrt.offline_schedule;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.TrainSchedule;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;
import io.github.shredktp.trainschedulesrt.show_schedule.ScheduleRecyclerViewAdapter;

public class OfflineScheduleActivity extends AppCompatActivity {

    private static final String TOOLBAR_TITLE = "Train Schedule";
    private static final String EXTRA_START_STATION = "startStation";
    private static final String EXTRA_END_STATION = "endStation";

    private Toolbar toolbar;

    private RecyclerView recyclerViewOfflineSchedule;
    private RecyclerView.Adapter adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    private DividerItemDecoration mDividerItemDecoration;

    private TextView tvTitleStartStation, tvTitleEndStation;

    private ArrayList<TrainSchedule> trainScheduleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_schedule);

        String startStation = getIntent().getStringExtra(EXTRA_START_STATION);
        String endStation = getIntent().getStringExtra(EXTRA_END_STATION);

        setupView(startStation, endStation);
        setupToolbar();
        setupData(startStation, endStation);
        setupListView();
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
        if (actionBar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupData(String startStation, String endStation) {
        trainScheduleArrayList = TrainScheduleLocalDataSource.getInstance(getApplicationContext()).getTrainScheduleByStation(startStation, endStation);
    }

    private void setupListView() {
        layoutManagerRecyclerView = new LinearLayoutManager(this);
        recyclerViewOfflineSchedule.setLayoutManager(layoutManagerRecyclerView);
        mDividerItemDecoration = new DividerItemDecoration(recyclerViewOfflineSchedule.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewOfflineSchedule.addItemDecoration(mDividerItemDecoration);

        adapterRecyclerView = new ScheduleRecyclerViewAdapter(trainScheduleArrayList);
        recyclerViewOfflineSchedule.setAdapter(adapterRecyclerView);
    }
}
