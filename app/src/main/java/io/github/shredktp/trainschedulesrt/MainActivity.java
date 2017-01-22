package io.github.shredktp.trainschedulesrt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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
        Call<String> trainScheduleBodyCall = srtApi.getSchedule("", "");
//        Call<String> trainScheduleBodyCall = srtApi.getSchedule("กรุงเทพ", "อยุธยา");

        trainScheduleBodyCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse Successful: " + response.body());
                    Log.d(TAG, "onResponse Successful: " + response.message());
                    Log.d(TAG, "onResponse Successful: " + response.toString());
                    Log.d(TAG, "onResponse Successful: " + response.headers());
                    Log.d(TAG, "onResponse Successful: " + response.errorBody());
                    tvHelloWorld.setText(response.body());
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

    private void setupView() {
        tvHelloWorld = (TextView) findViewById(R.id.tv_hello_world);
    }

//    private void setupWebViewFragment() {
//        ScheduleResultWebViewFragment scheduleResultWebViewFragment = ScheduleResultWebViewFragment.newInstance("", "");
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_web_view_schedule, scheduleResultWebViewFragment);
//        fragmentTransaction.commit();
//    }


//    if (response.isSuccessful()) {
//        Log.d(TAG, "onResponse Successful: " + response.body());
//        tvHelloWorld.setText(response.body());
//    } else {
//        Log.d(TAG, "onResponse not successful: " + response.body());
//        Log.d(TAG, "onResponse not successful: " + response.errorBody());
//    }


//    Log.d(TAG, "onFailure: " + t.getMessage());
//    tvHelloWorld.setText(t.getMessage());
}
