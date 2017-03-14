package io.github.shredktp.trainschedulesrt.history_search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationLocalDataSource;
import io.github.shredktp.trainschedulesrt.show_schedule.MainActivity;

public class HistoryActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private RecyclerView recyclerViewPairStation;
    private RecyclerView.Adapter adapterRecyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    private DividerItemDecoration mDividerItemDecoration;
    private ArrayList<PairStation> pairStationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupToolbar();
        setupNavigationDrawer();
        setupView();

        pairStationArrayList = PairStationLocalDataSource.getInstance(Contextor.getInstance().getContext()).getAllPairStation();
        setupRecyclerView(pairStationArrayList);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        toolbar.setTitle("History Search");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationDrawer() {
        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.history_drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorAccent);
        NavigationView navigationView = (NavigationView) findViewById(R.id.history_nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
            navigationView.getMenu().getItem(1).setChecked(true);
        }
    }

    private void setupView() {
        recyclerViewPairStation = (RecyclerView) findViewById(R.id.recycler_view_history);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.main_navigation_menu_item:
                                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.history_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.setting_navigation_menu_item:
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void setupRecyclerView(ArrayList<PairStation> pairStationArrayList) {
        layoutManagerRecyclerView = new LinearLayoutManager(this);
        recyclerViewPairStation.setLayoutManager(layoutManagerRecyclerView);
        mDividerItemDecoration = new DividerItemDecoration(recyclerViewPairStation.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewPairStation.addItemDecoration(mDividerItemDecoration);

        adapterRecyclerView = new HistoryRecyclerAdapter(pairStationArrayList, HistoryActivity.this);
        recyclerViewPairStation.setAdapter(adapterRecyclerView);

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
