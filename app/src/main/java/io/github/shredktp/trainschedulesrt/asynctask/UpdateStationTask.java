package io.github.shredktp.trainschedulesrt.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import io.github.shredktp.trainschedulesrt.data.StationDataSource;
import io.github.shredktp.trainschedulesrt.data.StationDataSourceImpl;
import io.github.shredktp.trainschedulesrt.model.Station;

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
        StationDataSource stationDataSource = new StationDataSourceImpl(context);
        stationDataSource.addStation(stations);
        return null;
    }
}