package io.github.shredktp.trainschedulesrt.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.TrainSchedule;

/**
 * Created by Korshreddern on 28-Jan-17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "srtDb.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String createTableStation = String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT);",
            Station.STATION_TABLE_NAME,
            Station.Column.ID,
            Station.Column.NAME,
            Station.Column.LINE);

    private String createTableTrainSchedule = String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
            TrainSchedule.TRAIN_SCHEDULE_TABLE_NAME,
            TrainSchedule.Column.ID,
            TrainSchedule.Column.NAME,
            TrainSchedule.Column.TYPE,
            TrainSchedule.Column.START_TIME,
            TrainSchedule.Column.END_TIME);

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableStation);
        sqLiteDatabase.execSQL(createTableTrainSchedule);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropTableStation = "DROP TABLE IF EXISTS " + Station.STATION_TABLE_NAME;
        String dropTableTrainSchedule = "DROP TABLE IF EXISTS " + TrainSchedule.TRAIN_SCHEDULE_TABLE_NAME;
        sqLiteDatabase.execSQL(dropTableStation);
        sqLiteDatabase.execSQL(dropTableTrainSchedule);
        onCreate(sqLiteDatabase);
    }
}
