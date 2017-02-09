package io.github.shredktp.trainschedulesrt.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.source.local.StationLocalDataSource;

/**
 * Created by Korshreddern on 29-Jan-17.
 */

public class UpdateStationTask extends AsyncTask<Station, Void, Void> {

    private Context context;

    public UpdateStationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Station... stations) {
//        StationDataSource stationDataSource = new StationLocalDataSource(context);
//        stationDataSource.addStation(stations);
        StationLocalDataSource.getInstance(Contextor.getInstance().getContext()).addStation(stations);
        return null;
    }
}