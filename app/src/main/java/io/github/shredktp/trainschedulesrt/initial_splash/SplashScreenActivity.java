package io.github.shredktp.trainschedulesrt.initial_splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.Utils.ConnectionUtil;
import io.github.shredktp.trainschedulesrt.api_srt.ApiSrt;
import io.github.shredktp.trainschedulesrt.api_srt.ToStringConverterFactory;
import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.source.station.StationLocalDataSource;
import io.github.shredktp.trainschedulesrt.show_schedule.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static io.github.shredktp.trainschedulesrt.show_schedule.MainActivity.BASE_URL_SRT_CHECK_TIME;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private static final int COUNT_ALL_STATION = 670;
    private static final String NO_STATION = "Cannot retrieve information, please connect internet and try again";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int countStation = StationLocalDataSource.getInstance(Contextor.getInstance().getContext())
                .countStation();

        if (countStation <= COUNT_ALL_STATION) { //680
            Log.d(TAG, "onCreate: Setup Station");

            if (ConnectionUtil.isConnected(Contextor.getInstance().getContext())) {
                stationApiRequester();
            } else {
                // TODO: 27-Feb-17 Display TextView problem
                // TODO: 27-Feb-17 Display Button Try Again
            }
        } else {
            Log.d(TAG, "onCreate: " + countStation + " Stations");
            gotoMainAct();
        }
    }

    private void gotoMainAct() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

                    // TODO: 27-Feb-17 Case 1) HTML Parser Error -> Display Button Goto SRT Web
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
                Toast.makeText(SplashScreenActivity.this, NO_STATION, Toast.LENGTH_SHORT).show();

                // TODO: 27-Feb-17 Case 1) Wifi login after connected -> Display Button Try Again
                // TODO: 27-Feb-17 Case 2) API Error -> Display Button Goto SRT Web

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
        new UpdateStationArrayListTask().execute(stationArrayList);
    }

    public class UpdateStationArrayListTask extends AsyncTask<ArrayList<Station>, Void, Void> {

        private static final String TAG = "UdtStationTask";

        UpdateStationArrayListTask() {
        }

        @Override
        protected Void doInBackground(ArrayList<Station>... stations) {
            StationLocalDataSource.getInstance(Contextor.getInstance().getContext()).deleteAllStation();
            StationLocalDataSource.getInstance(Contextor.getInstance().getContext()).addStation(stations[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: Finish");
            gotoMainAct();
        }
    }
}
