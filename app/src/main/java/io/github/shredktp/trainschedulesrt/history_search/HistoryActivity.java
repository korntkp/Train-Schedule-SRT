package io.github.shredktp.trainschedulesrt.history_search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationLocalDataSource;
import io.github.shredktp.trainschedulesrt.show_schedule.MainActivity;

public class HistoryActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView listViewPairStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupToolbar();
        setupNavigationDrawer();
        setupView();

        ArrayList<PairStation> pairStationArrayList = PairStationLocalDataSource.getInstance(Contextor.getInstance().getContext()).getAllPairStation();
        setupResult(pairStationArrayList);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        toolbar.setTitle("History Search");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigationDrawer() {
        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.history_drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorAccent);
        NavigationView navigationView = (NavigationView) findViewById(R.id.history_nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.statistics_navigation_menu_item:
                                // Do nothing, we're already on that screen
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

    private void setupView() {
        listViewPairStation = (ListView) findViewById(R.id.list_view_history);
    }

    private void setupResult(ArrayList<PairStation> pairStationArrayList) {
        PairStationAdapter pairStationAdapter = new PairStationAdapter(Contextor.getInstance().getContext(), pairStationArrayList);
        listViewPairStation.setAdapter(pairStationAdapter);
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
}
