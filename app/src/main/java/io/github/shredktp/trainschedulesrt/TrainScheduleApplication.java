package io.github.shredktp.trainschedulesrt;

import android.app.Application;

/**
 * Created by Korshreddern on 08-Feb-17.
 */

public class TrainScheduleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }
}
