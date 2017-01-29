package io.github.shredktp.trainschedulesrt.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.StationDataSource;
import io.github.shredktp.trainschedulesrt.data.StationDataSourceImpl;
import io.github.shredktp.trainschedulesrt.model.Station;

/**
 * Created by Korshreddern on 29-Jan-17.
 */

public class UpdateStationTask extends AsyncTask<ArrayList<Station>, Void, Void> {

    private Context context;

    public UpdateStationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(ArrayList<Station>... arrayLists) {
        StationDataSource stationDataSource = new StationDataSourceImpl(context);
        stationDataSource.addStation(arrayLists[0]);
        return null;
    }
}