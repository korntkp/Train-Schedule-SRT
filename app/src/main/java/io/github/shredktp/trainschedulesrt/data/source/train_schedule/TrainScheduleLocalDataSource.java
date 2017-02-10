package io.github.shredktp.trainschedulesrt.data.source.train_schedule;

import android.content.Context;

import io.github.shredktp.trainschedulesrt.data.source.DbHelper;

/**
 * Created by Korshreddern on 10-Feb-17.
 */

public class TrainScheduleLocalDataSource implements TrainScheduleDataSource {
    private static final String TAG = "TrSchdlDSrc";

    private static TrainScheduleLocalDataSource INSTANCE;
    private DbHelper dbHelper;

    public static TrainScheduleLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TrainScheduleLocalDataSource(context);
        }
        return INSTANCE;
    }

    private TrainScheduleLocalDataSource(Context context) {
        this.dbHelper = new DbHelper(context);
    }

}
