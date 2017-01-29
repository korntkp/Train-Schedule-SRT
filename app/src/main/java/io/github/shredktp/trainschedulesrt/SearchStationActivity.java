package io.github.shredktp.trainschedulesrt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SearchStationActivity extends AppCompatActivity {

    Button btnStation;
    int req = 99999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        Intent intent = getIntent();
        req = intent.getIntExtra("req", -1);

        btnStation = (Button) findViewById(R.id.btn_station);
        btnStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                if (req == 12794) {
                    resultIntent.putExtra("station", "กรุงเทพ");
                } else if (req == 12795) {
                    resultIntent.putExtra("station", "อยุธยา");
                } else {
                    resultIntent.putExtra("station", "-");
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
