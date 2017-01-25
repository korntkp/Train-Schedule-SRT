package io.github.shredktp.trainschedulesrt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.model.TrainSchedule;
import io.github.shredktp.trainschedulesrt.srt_api.SrtApi;
import io.github.shredktp.trainschedulesrt.srt_api.ToStringConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL_SRT_CHECK_TIME = "http://www.railway.co.th/checktime/";

    TextView tvHelloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        setupApiCaller();
//        setupWebViewFragment();
    }

    private void setupApiCaller() {
//        Gson gson = new GsonBuilder()
//                .disableHtmlEscaping()
//                .setLenient()
//                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SRT_CHECK_TIME)
                .addConverterFactory(new ToStringConverterFactory())
                .build();

        SrtApi srtApi = retrofit.create(SrtApi.class);
//        Call<String> trainScheduleBodyCall = srtApi.getSchedule("", "");
        Call<String> trainScheduleBodyCall = srtApi.getSchedule("กรุงเทพ", "อยุธยา");

        trainScheduleBodyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse Successful: " + response.message());
                    Log.d(TAG, "onResponse Successful: " + response.toString());
                    Log.d(TAG, "onResponse Successful: " + response.headers());
                    Log.d(TAG, "onResponse Successful: " + response.errorBody());

                    String result = response.body();
                    responseParser(result);

                } else {
                    Log.d(TAG, "onResponse not successful: " + response.body());
                    Log.d(TAG, "onResponse not successful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: " + t.getStackTrace());
                Log.d(TAG, "onFailure: " + call.request().toString());
                Log.d(TAG, "onFailure: " + call.request().body().contentType());
                Log.d(TAG, "onFailure: " + call.request().body().toString());
                tvHelloWorld.setText(t.getMessage());
            }
        });
    }

    private void responseParser(String html) {
        if (html.contains("ไม่มีขบวนรถที่จอดระหว่างสถานีต้นทาง และปลายทางที่ท่านเลือก")) {
            tvHelloWorld.setText("ไม่มีขบวนรถที่จอดระหว่างสถานีต้นทาง และปลายทางที่ท่านเลือก");
            return;
        }

        Document document = Jsoup.parse(html);
        Element body = document.body();
        Element divMainContent = body.child(1);
        Element table = divMainContent.select("table").get(0);
        Element tbody = table.select("tbody").get(0);
        Elements trElements = tbody.getElementsByTag("tr");

        ArrayList<TrainSchedule> trainScheduleArrayList = new ArrayList<>();
        for (Element tr: trElements) {
            Element trainNum = tr.select("div").get(1);
            Element trainType = tr.select("div").get(2);
            Element leaveTime = tr.select("div").get(3);
            Element arriveTime = tr.select("div").get(4);
            trainScheduleArrayList.add(new TrainSchedule(trainNum.text(), trainType.text(), leaveTime.text(), arriveTime.text()));
        }

        String result = "";
        for (int i = 0; i < trainScheduleArrayList.size(); i++) {
            Log.d(TAG, "responseParser: " + trainScheduleArrayList.get(i).toString());
            result += "รถออก: " + trainScheduleArrayList.get(i).getLeaveTime() + "\n";
        }
        tvHelloWorld.setText(result);
    }

    private void setupView() {
        tvHelloWorld = (TextView) findViewById(R.id.tv_hello_world);
    }
}
