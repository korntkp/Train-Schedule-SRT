package io.github.shredktp.trainschedulesrt.offline_schedule;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.TrainSchedule;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;
import io.github.shredktp.trainschedulesrt.show_schedule.ScheduleAdapter;

public class OfflineScheduleActivity extends AppCompatActivity {

    private static final String TOOLBAR_TITLE = "Train Schedule";
    private static final String EXTRA_START_STATION = "startStation";
    private static final String EXTRA_END_STATION = "endStation";

    private ListView offlineListViewSchedule;
    private Toolbar toolbar;
    private ArrayList<TrainSchedule> trainScheduleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_schedule);

        String startStation = getIntent().getStringExtra(EXTRA_START_STATION);
        String endStation = getIntent().getStringExtra(EXTRA_END_STATION);

        setupView();
        setupToolbar();
        setupData(startStation, endStation);
        setupListView();
    }

    private void setupData(String startStation, String endStation) {
        trainScheduleArrayList = TrainScheduleLocalDataSource.getInstance(getApplicationContext()).getTrainScheduleByStation(startStation, endStation);
    }

    private void setupView() {
        toolbar = (Toolbar) findViewById(R.id.offline_schedule_toolbar);
        offlineListViewSchedule = (ListView) findViewById(R.id.offline_schedule_list_view_schedule);
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

    private void setupListView() {
        ScheduleAdapter scheduleAdapter =
                new ScheduleAdapter(Contextor.getInstance().getContext(), trainScheduleArrayList);
//        scheduleAdapter.notifyDataSetChanged();
        offlineListViewSchedule.setAdapter(scheduleAdapter);
    }

//    private void setupNavigationDrawer() {
//        // Set up the navigation drawer.
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null) {
//            setupDrawerContent(navigationView);
//        }
//    }

//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.main_navigation_menu_item:
//                                // Do nothing, we're already on that screen
//                                break;
//                            case R.id.history_navigation_menu_item:
//                                Intent intent =
//                                        new Intent(OfflineScheduleActivity.this, HistoryActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                break;
//                            case R.id.setting_navigation_menu_item:
//                                break;
//                            default:
//                                break;
//                        }
//                        // Close the navigation drawer when an item is selected.
//                        menuItem.setChecked(true);
//                        drawerLayout.closeDrawers();
//                        return true;
//                    }
//                });
//    }
}
