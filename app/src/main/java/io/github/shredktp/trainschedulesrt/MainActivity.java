package io.github.shredktp.trainschedulesrt;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.StationDataSource;
import io.github.shredktp.trainschedulesrt.data.StationDataSourceImpl;
import io.github.shredktp.trainschedulesrt.model.Station;
import io.github.shredktp.trainschedulesrt.model.TrainSchedule;
import io.github.shredktp.trainschedulesrt.srt_api.SrtApi;
import io.github.shredktp.trainschedulesrt.srt_api.ToStringConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.shredktp.trainschedulesrt.R.id.btn_end;
import static io.github.shredktp.trainschedulesrt.R.id.btn_go;
import static io.github.shredktp.trainschedulesrt.R.id.btn_start;

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

        setupView();
        stationApiRequester();
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
                Log.d(TAG, "onFailure: " + t.getStackTrace());
                Log.d(TAG, "onFailure: " + call.request().toString());
//                Log.d(TAG, "onFailure: " + call.request().body().contentType());
//                Log.d(TAG, "onFailure: " + call.request().body().toString());
                tvDetail.setText(t.getMessage());
            }
        });
    }

    private void stationParser(String html) {
//        Log.d(TAG, "stationParser: " + html);
        Log.d(TAG, "stationParser: starting parse");
        Document document = Jsoup.parse(html);
        Element body = document.body();
        Element divMainContent = body.child(0);
        Element table = divMainContent.select("table").get(2);
        Element tr = table.child(0);
        Elements optionElements = tr.getElementsByTag("option");

        ArrayList<Station> stationArrayList = new ArrayList<>();
        for (Element option : optionElements) {
            String optionValue = option.attr("value");
//            String optionDisplay = option.text();
            stationArrayList.add(new Station(optionValue));
        }

        Log.d(TAG, "stationParser: starting setText");
        setStationToTextview(stationArrayList);

        Log.d(TAG, "stationParser: starting add to db");
        addStation(stationArrayList);
    }

//    private void setTextFromDb() {
//        StationDataSource stationDataSource = new StationDataSourceImpl(getApplicationContext());
//        ArrayList<Station> stationArrayList = stationDataSource.getAllStation();
//
//        setStationToTextview(stationArrayList);
//    }

    private void setStationToTextview(ArrayList<Station> stationArrayList) {
        String result = "";
        for (int i = 0; i < stationArrayList.size(); i++) {
            result += "Name: " + stationArrayList.get(i).getName() + "\n";
        }
        tvStation.setText(result);
    }

    private void addStation(ArrayList<Station> stationArrayList) {
        new LoadViewTask().execute(stationArrayList);
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
                Log.d(TAG, "onFailure: " + t.getStackTrace());
                Log.d(TAG, "onFailure: " + call.request().toString());
//                Log.d(TAG, "onFailure: " + call.request().body().contentType());
//                Log.d(TAG, "onFailure: " + call.request().body().toString());
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

    private void setupView() {
        btnGo = (Button) findViewById(btn_go);
        btnStart = (Button) findViewById(btn_start);
        btnEnd = (Button) findViewById(btn_end);
//        edtStart = (EditText) findViewById(R.id.edt_start);
//        edtEnd = (EditText) findViewById(R.id.edt_end);
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
                Intent intent = new Intent(MainActivity.this, SearchStationActivity.class);
                intent.putExtra("req", 12794);
                startActivityForResult(intent, 12794);
                break;
            }
            case btn_end: {
                Intent intent = new Intent(MainActivity.this, SearchStationActivity.class);
                intent.putExtra("req", 12795);
                startActivityForResult(intent, 12795);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12794) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("station");
                btnStart.setText(result);
            }
        } else if (requestCode == 12795) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("station");
                btnEnd.setText(result);
            }
        }
    }//onActivityResult

    private class LoadViewTask extends AsyncTask<ArrayList<Station>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<Station>... arrayLists) {
            StationDataSource stationDataSource = new StationDataSourceImpl(getApplicationContext());
            stationDataSource.addStation(arrayLists[0]);
            return null;
        }
    }
}
