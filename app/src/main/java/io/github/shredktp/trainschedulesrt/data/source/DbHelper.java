package io.github.shredktp.trainschedulesrt.data.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationPersistenceContract.PairStationEntry;
import io.github.shredktp.trainschedulesrt.data.source.station.StationPersistenceContract.StationEntry;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainSchedulePersistenceContract.TrainScheduleEntry;

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
            StationEntry.TABLE_NAME,
            StationEntry.COLUMN_NAME_ENTRY_ID,
            StationEntry.COLUMN_NAME_NAME,
            StationEntry.COLUMN_NAME_LINE);

    private String createTableTrainSchedule = String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
            TrainScheduleEntry.TABLE_NAME,
            TrainScheduleEntry.COLUMN_NAME_ENTRY_ID,
            TrainScheduleEntry.COLUMN_NAME_STAR_END_STATION,
            TrainScheduleEntry.COLUMN_NAME_NUMBER,
            TrainScheduleEntry.COLUMN_NAME_TYPE,
            TrainScheduleEntry.COLUMN_NAME_START_TIME,
            TrainScheduleEntry.COLUMN_NAME_END_TIME);

    private String createTablePairStation = String.format("CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
            PairStationEntry.TABLE_NAME,
            PairStationEntry.COLUMN_NAME_ENTRY_ID,
            PairStationEntry.COLUMN_NAME_START_STATION,
            PairStationEntry.COLUMN_NAME_END_STATION,
            PairStationEntry.COLUMN_NAME_IS_FIRST,
            PairStationEntry.COLUMN_NAME_TIMESTAMP);

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTableStation);
        sqLiteDatabase.execSQL(createTableTrainSchedule);
        sqLiteDatabase.execSQL(createTablePairStation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropTableStation = "DROP TABLE IF EXISTS " + StationEntry.TABLE_NAME;
        String dropTableTrainSchedule = "DROP TABLE IF EXISTS " + TrainScheduleEntry.TABLE_NAME;
        String dropTablePairStation = "DROP TABLE IF EXISTS " + PairStationEntry.TABLE_NAME;

        sqLiteDatabase.execSQL(dropTableStation);
        sqLiteDatabase.execSQL(dropTableTrainSchedule);
        sqLiteDatabase.execSQL(dropTablePairStation);
        onCreate(sqLiteDatabase);
    }
}
