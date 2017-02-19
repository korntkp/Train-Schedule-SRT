package io.github.shredktp.trainschedulesrt.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.source.station.StationLocalDataSource;

/**
 * Created by Korshreddern on 29-Jan-17.
 */

public class UpdateStationArrayListTask extends AsyncTask<ArrayList<Station>, Void, Void> {

    private static final String TAG = "UdtStationTask";

    public UpdateStationArrayListTask() {
    }

    @Override
    protected Void doInBackground(ArrayList<Station>... stations) {
//        StationDataSource stationDataSource = new StationLocalDataSource(context);
//        stationDataSource.addStation(stations[0]);
        StationLocalDataSource.getInstance(Contextor.getInstance().getContext()).addStation(stations[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "onPostExecute: Finish");
    }
}