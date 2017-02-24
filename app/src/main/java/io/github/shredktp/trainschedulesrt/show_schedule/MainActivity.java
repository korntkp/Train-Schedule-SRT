package io.github.shredktp.trainschedulesrt.show_schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.Utils.IdlingResourceImpl;
import io.github.shredktp.trainschedulesrt.api_srt.ApiSrt;
import io.github.shredktp.trainschedulesrt.api_srt.ToStringConverterFactory;
import io.github.shredktp.trainschedulesrt.asynctask.UpdateStationArrayListTask;
import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.TrainSchedule;
import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationLocalDataSource;
import io.github.shredktp.trainschedulesrt.data.source.station.StationLocalDataSource;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;
import io.github.shredktp.trainschedulesrt.history_search.HistoryActivity;
import io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.shredktp.trainschedulesrt.R.id.btn_see_schedule;
import static io.github.shredktp.trainschedulesrt.R.id.btn_select_end_station;
import static io.github.shredktp.trainschedulesrt.R.id.btn_select_start_station;
import static io.github.shredktp.trainschedulesrt.R.id.fab_see_it_first;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.EXTRA_KEY_REQUEST_CODE;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.INTENT_EXTRA_KEY_STATION;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.REQUEST_CODE_END_STATION;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.REQUEST_CODE_START_STATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL_SRT_CHECK_TIME = "http://www.railway.co.th/checktime/";
    private static final String NO_SCHEDULE = "ไม่มีขบวนรถที่จอดระหว่างสถานีต้นทาง และปลายทางที่ท่านเลือก";
    public static final String START_STATION = "startStation";
    public static final String END_STATION = "endStation";
    private static final int COUNT_ALL_STATION = 670;

    private Button btnSeeSchedule;
    private Button btnSelectStartStation, btnSelectEndStation;
    private FloatingActionButton fabSeeItFirst;

    private ListView listViewSchedule;
    private LinearLayout linearLayoutDetail;
    private TextView tvDetail;
    private DrawerLayout drawerLayout;

    private String startStation = "";
    private String endStation = "";

    private IdlingResourceImpl idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: MainActivity");
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupNavigationDrawer();
        setupView();

        int countStation = StationLocalDataSource.getInstance(Contextor.getInstance().getContext())
                .countStation();
        if (countStation <= COUNT_ALL_STATION) { //680
            Log.d(TAG, "onCreate: Setup Station");
            // TODO: 21-Feb-17 Clear All Station
            stationApiRequester();
        } else {
            setupSeeItFirst();
        }
    }

    private void setupSeeItFirst() {
        PairStation pairStation;
        try {
            pairStation = PairStationLocalDataSource
                    .getInstance(Contextor.getInstance().getContext()).getSeeFirstPairStation();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        ArrayList<TrainSchedule> trainScheduleArrayList = TrainScheduleLocalDataSource.getInstance(Contextor.getInstance().getContext()).getTrainScheduleByStation(pairStation.getStartStation(), pairStation.getEndStation());
        setupScheduleResult(trainScheduleArrayList);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationDrawer() {
        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupView() {
        btnSeeSchedule = (Button) findViewById(btn_see_schedule);
        btnSelectStartStation = (Button) findViewById(btn_select_start_station);
        btnSelectEndStation = (Button) findViewById(btn_select_end_station);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        fabSeeItFirst = (FloatingActionButton) findViewById(fab_see_it_first);

        listViewSchedule = (ListView) findViewById(R.id.list_view_schedule);
        linearLayoutDetail = (LinearLayout) findViewById(R.id.layout_detail);

        btnSeeSchedule.setOnClickListener(this);
        btnSelectStartStation.setOnClickListener(this);
        btnSelectEndStation.setOnClickListener(this);
        fabSeeItFirst.setOnClickListener(this);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.main_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.history_navigation_menu_item:
                                Intent intent =
                                        new Intent(MainActivity.this, HistoryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                            case R.id.setting_navigation_menu_item:
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void stationApiRequester() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SRT_CHECK_TIME)
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        ApiSrt apiSrt = retrofit.create(ApiSrt.class);
        Call<String> stationCaller = apiSrt.getStation();

        stationCaller.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "stationCaller onResponse: isSuccessful");
                    String result = response.body();
                    stationParser(result);
                } else {
                    Log.d(TAG, "stationApiRequester onResponse not successful: " + response.body());
                    Log.d(TAG, "onResponse not successful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "stationApiRequester onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: " + call.request().toString());
                Toast.makeText(MainActivity.this, NO_SCHEDULE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stationParser(String html) {
        Log.d(TAG, "stationParser: starting parse");
        ArrayList<Station> stationArrayList = stationExtractor(html);
        addStationByArrayList(stationArrayList);
//        addStationByArray(stations);
    }

    @NonNull
    private ArrayList<Station> stationExtractor(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();
        Element divMainContent = body.child(0);
        Element table = divMainContent.select("table").get(2);
        Element tr = table.child(0);
        Element tbodytr = tr.child(1);
        Element elementStation = tbodytr.getElementById("StationFirst");
        Elements optionElements = elementStation.getElementsByTag("option");

        ArrayList<Station> stationArrayList = new ArrayList<>();
        for (Element option : optionElements) {
            String optionValue = option.attr("value");
            if (!optionValue.equals("")) {
                stationArrayList.add(new Station(optionValue));
            }
        }
        return stationArrayList;
    }

//    private void addStationByArray(Station[] stations) {
//        new UpdateStationTask(getApplicationContext()).execute(stations);
//    }

    private void addStationByArrayList(ArrayList<Station> stationArrayList) {
        Log.d(TAG, "addStationByArrayList Size: " + stationArrayList.size());
        // TODO: 21-Feb-17 Take a long time !!!
        new UpdateStationArrayListTask().execute(stationArrayList);
    }

    private void trainScheduleApiRequester(final String startStation, final String endStation) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SRT_CHECK_TIME)
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        ApiSrt apiSrt = retrofit.create(ApiSrt.class);
        final Call<String> trainScheduleBodyCall = apiSrt.getSchedule(startStation, endStation);

        // TODO: 24-Feb-17 Espresso Idling Resource (xxx, this, idlingResource)
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        trainScheduleBodyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    if (responseBody.contains(NO_SCHEDULE)) {
                        setupNoScheduleResult();
                    } else {
                        ArrayList<TrainSchedule> trainScheduleArrayList = scheduleExtractor(startStation, endStation, responseBody);
                        saveHistoryAndSchedule(trainScheduleArrayList);
                        setupScheduleResult(trainScheduleArrayList);
                        fabSeeItFirst.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "onResponse not successful: " + response.body());
                    Log.d(TAG, "onResponse not successful: " + response.errorBody());
                }
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "trainScheduleApiRequester onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: " + call.request().toString());
                setupNoScheduleResult();
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        });
    }

    private void saveHistoryAndSchedule(ArrayList<TrainSchedule> trainScheduleArrayList) {
        PairStation pairStation = new PairStation(startStation, endStation, 1, 0, System.currentTimeMillis());
        PairStationLocalDataSource.getInstance(Contextor.getInstance().getContext()).add(pairStation);
        TrainScheduleLocalDataSource.getInstance(Contextor.getInstance().getContext()).add(trainScheduleArrayList);
    }

    private void setupScheduleResult(ArrayList<TrainSchedule> trainScheduleArrayList) {
        linearLayoutDetail.setVisibility(View.GONE);
        listViewSchedule.setVisibility(View.VISIBLE);
        ScheduleAdapter scheduleAdapter =
                new ScheduleAdapter(Contextor.getInstance().getContext(), trainScheduleArrayList);
        scheduleAdapter.notifyDataSetChanged();
        listViewSchedule.setAdapter(scheduleAdapter);
    }

    private void setupNoScheduleResult() {
        listViewSchedule.setVisibility(View.GONE);
        linearLayoutDetail.setVisibility(View.VISIBLE);
        tvDetail.setText(NO_SCHEDULE);
        Toast.makeText(MainActivity.this, "Phasing HTML Error", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private ArrayList<TrainSchedule> scheduleExtractor(String startStation, String endStation, String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();
        Element divMainContent = body.child(1);
        Element table = divMainContent.select("table").get(0);
        Element tbody = table.select("tbody").get(0);
        Elements trElements = tbody.getElementsByTag("tr");

        ArrayList<TrainSchedule> trainScheduleArrayList = new ArrayList<>();
        for (Element tr : trElements) {
            Element trainNum = tr.select("div").get(1);
            Element trainType = tr.select("div").get(2);
            Element startTime = tr.select("div").get(3);
            Element endTime = tr.select("div").get(4);
            trainScheduleArrayList.add(
                    new TrainSchedule(startStation,
                            endStation,
                            trainNum.text(),
                            trainType.text(),
                            startTime.text(),
                            endTime.text()));
        }
        return trainScheduleArrayList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case btn_select_start_station: {
                fabSeeItFirst.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, SelectStationActivity.class);
                intent.putExtra(EXTRA_KEY_REQUEST_CODE, REQUEST_CODE_START_STATION);
                startActivityForResult(intent, REQUEST_CODE_START_STATION);
                break;
            }
            case btn_select_end_station: {
                fabSeeItFirst.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, SelectStationActivity.class);
                intent.putExtra(EXTRA_KEY_REQUEST_CODE, REQUEST_CODE_END_STATION);
                startActivityForResult(intent, REQUEST_CODE_END_STATION);
                break;
            }
            case btn_see_schedule: {
                trainScheduleApiRequester(startStation, endStation);
                break;
            }
            case fab_see_it_first: {
                PairStation pairStation = new PairStation(startStation, endStation, 0, 1, 0);
                PairStationLocalDataSource.getInstance(Contextor.getInstance().getContext())
                        .updateSeeItFirst(pairStation);
                Snackbar.make(view, "This schedule is bookmarked", Snackbar.LENGTH_SHORT).show();
                fabSeeItFirst.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_START_STATION) {
            if (resultCode == Activity.RESULT_OK) {
                startStation = data.getStringExtra(INTENT_EXTRA_KEY_STATION);
                btnSelectStartStation.setText(startStation);
            }
        } else if (requestCode == REQUEST_CODE_END_STATION) {
            if (resultCode == Activity.RESULT_OK) {
                endStation = data.getStringExtra(INTENT_EXTRA_KEY_STATION);
                btnSelectEndStation.setText(endStation);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        startStation = savedInstanceState.getString(START_STATION);
        endStation = savedInstanceState.getString(END_STATION);
        if (!startStation.equals("")) btnSelectStartStation.setText(startStation);
        if (!endStation.equals("")) btnSelectEndStation.setText(endStation);

//        if (!startStation.equals("") && !endStation.equals(""))
//            trainScheduleApiRequester(startStation, endStation);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(START_STATION, startStation);
        outState.putString(END_STATION, endStation);
        super.onSaveInstanceState(outState);
    }

    /*
    * UI Testing
    * Espresso Idling
    * */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new IdlingResourceImpl();
        }
        return idlingResource;
    }
}
