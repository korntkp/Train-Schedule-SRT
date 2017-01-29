package io.github.shredktp.trainschedulesrt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static io.github.shredktp.trainschedulesrt.R.id.btn_station;

public class SearchStationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_START_STATION = 12794;
    public static final int REQUEST_CODE_END_STATION = 12795;
    public static final String EXTRA_KEY_REQUEST_CODE = "request_c";
    public static final String INTENT_EXTRA_KEY_STATION = "station";

    Button btnStation;
    int req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        getExtraFromIntent();
        setupView();
    }

    private void getExtraFromIntent() {
        Intent intent = getIntent();
        req = intent.getIntExtra(EXTRA_KEY_REQUEST_CODE, -1);
    }

    private void setupView() {
        btnStation = (Button) findViewById(btn_station);
        btnStation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case btn_station: {
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
}
