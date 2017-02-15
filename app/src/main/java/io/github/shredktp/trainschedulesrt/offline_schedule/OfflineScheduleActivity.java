package io.github.shredktp.trainschedulesrt.offline_schedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.github.shredktp.trainschedulesrt.R;

public class OfflineScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_schedule);
        String startStation = getIntent().getStringExtra("startStation");
        String endStation = getIntent().getStringExtra("endStation");
        Toast.makeText(this, startStation + " " + endStation, Toast.LENGTH_SHORT).show();
    }
}
