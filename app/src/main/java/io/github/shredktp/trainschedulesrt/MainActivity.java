package io.github.shredktp.trainschedulesrt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.api_srt.SrtApi;
import io.github.shredktp.trainschedulesrt.api_srt.ToStringConverterFactory;
import io.github.shredktp.trainschedulesrt.asynctask.UpdateStationArrayListTask;
import io.github.shredktp.trainschedulesrt.data.StationDataSource;
import io.github.shredktp.trainschedulesrt.data.StationDataSourceImpl;
import io.github.shredktp.trainschedulesrt.model.Station;
import io.github.shredktp.trainschedulesrt.model.TrainSchedule;
import io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.shredktp.trainschedulesrt.R.id.btn_end;
import static io.github.shredktp.trainschedulesrt.R.id.btn_go;
import static io.github.shredktp.trainschedulesrt.R.id.btn_start;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.EXTRA_KEY_REQUEST_CODE;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.INTENT_EXTRA_KEY_STATION;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.REQUEST_CODE_END_STATION;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.REQUEST_CODE_START_STATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL_SRT_CHECK_TIME = "http://www.railway.co.th/checktime/";

    Button btnGo;
    Button btnStart, btnEnd;
//    EditText edtStart, edtEnd;

    TextView tvDetail;
    TextView tvStation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupView();

        StationDataSource stationDataSource = new StationDataSourceImpl(getApplicationContext());
        if (stationDataSource.countStation() <= 0) {
            Log.d(TAG, "onCreate: Setup Station");
            stationApiRequester();
        }
    }

    private void stationApiRequester() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SRT_CHECK_TIME)
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        SrtApi srtApi = retrofit.create(SrtApi.class);
        Call<String> trainScheduleBodyCall = srtApi.getStation();

        trainScheduleBodyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "trainScheduleBodyCall onResponse: isSuccessful");
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
//                Log.d(TAG, "onFailure: " + call.request().body().contentType());
//                Log.d(TAG, "onFailure: " + call.request().body().toString());
                tvDetail.setText(t.getMessage());
            }
        });
    }

    private void stationParser(String html) {
        Log.d(TAG, "stationParser: starting parse");
        Document document = Jsoup.parse(html);
        Element body = document.body();
        Element divMainContent = body.child(0);
        Element table = divMainContent.select("table").get(2);
        Element tr = table.child(0);
        Element tbodytr = tr.child(1);
        Element elementStation = tbodytr.getElementById("StationFirst");
        Elements optionElements = elementStation.getElementsByTag("option");

        ArrayList<Station> stationArrayList = new ArrayList<>();
//        Station[] stations = new Station[optionElements.size()];
//        int i = 0;
        for (Element option : optionElements) {
            String optionValue = option.attr("value");
            Log.d(TAG, "stationParser: " + optionValue);
            if (!optionValue.equals("")) {
//            String optionDisplay = option.text();
                stationArrayList.add(new Station(optionValue));
//                stations[i++] = new Station(optionValue);
            }
        }

        Log.d(TAG, "stationParser: starting setText");
        setStationToTextview(stationArrayList);

        Log.d(TAG, "stationParser: starting add to db");
        addStationByArrayList(stationArrayList);
//        addStationByArray(stations);
    }

    private void setStationToTextview(ArrayList<Station> stationArrayList) {
        String result = "";
        for (int i = 0; i < stationArrayList.size(); i++) {
            result += "Name: " + stationArrayList.get(i).getName() + "\n";
        }
        tvStation.setText(result);
    }

//    private void addStationByArray(Station[] stations) {
//        new UpdateStationTask(getApplicationContext()).execute(stations);
//    }

    private void addStationByArrayList(ArrayList<Station> stationArrayList) {
        Log.d(TAG, "addStationByArrayList Size: " + stationArrayList.size());
        new UpdateStationArrayListTask(getApplicationContext()).execute(stationArrayList);
    }

    private void trainScheduleApiRequester(String startStation, String endStation) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SRT_CHECK_TIME)
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        SrtApi srtApi = retrofit.create(SrtApi.class);
        Call<String> trainScheduleBodyCall = srtApi.getSchedule(startStation, endStation);

        trainScheduleBodyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    trainScheduleParser(result);
                } else {
                    Log.d(TAG, "onResponse not successful: " + response.body());
                    Log.d(TAG, "onResponse not successful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "trainScheduleApiRequester onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: " + call.request().toString());
                tvDetail.setText(t.getMessage());
            }
        });
    }

    private void trainScheduleParser(String html) {
        if (html.contains("ไม่มีขบวนรถที่จอดระหว่างสถานีต้นทาง และปลายทางที่ท่านเลือก")) {
            tvDetail.setText("ไม่มีขบวนรถที่จอดระหว่างสถานีต้นทาง และปลายทางที่ท่านเลือก");
            return;
        }

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
            Element leaveTime = tr.select("div").get(3);
            Element arriveTime = tr.select("div").get(4);
            trainScheduleArrayList.add(new TrainSchedule(trainNum.text(), trainType.text(), leaveTime.text(), arriveTime.text()));
        }

        String result = "";
        for (int i = 0; i < trainScheduleArrayList.size(); i++) {
//            Log.d(TAG, "trainScheduleParser: " + trainScheduleArrayList.get(i).toString());
            result += "รถออก: " + trainScheduleArrayList.get(i).getStartTime() + "\n";
        }
        tvDetail.setText(result);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupView() {
        btnGo = (Button) findViewById(btn_go);
        btnStart = (Button) findViewById(btn_start);
        btnEnd = (Button) findViewById(btn_end);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvStation = (TextView) findViewById(R.id.tv_station);

//        btnStart.setText("กรุงเทพ");
//        btnEnd.setText("อยุธยา");

        btnGo.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case btn_go: {
                trainScheduleApiRequester(btnStart.getText().toString(), btnEnd.getText().toString());
                break;
            }
            case btn_start: {
                Intent intent = new Intent(MainActivity.this, SelectStationActivity.class);
                intent.putExtra(EXTRA_KEY_REQUEST_CODE, REQUEST_CODE_START_STATION);
                startActivityForResult(intent, REQUEST_CODE_START_STATION);
                break;
            }
            case btn_end: {
                Intent intent = new Intent(MainActivity.this, SelectStationActivity.class);
                intent.putExtra(EXTRA_KEY_REQUEST_CODE, REQUEST_CODE_END_STATION);
                startActivityForResult(intent, REQUEST_CODE_END_STATION);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_START_STATION) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(INTENT_EXTRA_KEY_STATION);
                btnStart.setText(result);
            }
        } else if (requestCode == REQUEST_CODE_END_STATION) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(INTENT_EXTRA_KEY_STATION);
                btnEnd.setText(result);
            }
        }
    }
}
