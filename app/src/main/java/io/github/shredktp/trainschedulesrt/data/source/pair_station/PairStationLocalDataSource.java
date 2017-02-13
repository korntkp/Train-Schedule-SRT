package io.github.shredktp.trainschedulesrt.data.source.pair_station;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.data.source.DbHelper;
import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationPersistenceContract.PairStationEntry;


/**
 * Created by Korshreddern on 11-Feb-17.
 */

public class PairStationLocalDataSource implements PairStationDataSource {
    private static final String TAG = "PairSttDSrc";

    private static PairStationLocalDataSource INSTANCE;
    private DbHelper dbHelper;

    public static PairStationLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PairStationLocalDataSource(context);
        }
        return INSTANCE;
    }

    private PairStationLocalDataSource(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    @Override
    public long add(PairStation pairStation) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PairStationEntry.COLUMN_NAME_START_STATION, pairStation.getStartStation());
        contentValues.put(PairStationEntry.COLUMN_NAME_END_STATION, pairStation.getEndStation());
        contentValues.put(PairStationEntry.COLUMN_NAME_IS_FIRST, pairStation.isSeeItFirst());
        contentValues.put(PairStationEntry.COLUMN_NAME_TIMESTAMP, pairStation.getTimestamp());
        long result = sqLiteDatabase.insert(PairStationEntry.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return result;
    }

    @Override
    public PairStation getSeeFirstPairStation() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String queryPairStation = String.format("SELECT * FROM %s WHERE %s == %s",
                PairStationEntry.TABLE_NAME,
                PairStationEntry.COLUMN_NAME_IS_FIRST,
                "true");

        Cursor cursor = sqLiteDatabase.rawQuery(queryPairStation, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getSeeFirstPairStation Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getSeeFirstPairStation: No item in PairStation Table");
        }

        PairStation pairStationResult = new PairStation(
                cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_START_STATION)),
                cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_END_STATION)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_IS_FIRST))),
                cursor.getLong(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_TIMESTAMP))
        );

        cursor.close();
        sqLiteDatabase.close();
        return pairStationResult;
    }

    @Override
    public ArrayList<PairStation> getAllPairStation() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<PairStation> pairStationArrayList = new ArrayList<>();

        String queryStation = String.format("SELECT * FROM %s",
                PairStationEntry.TABLE_NAME);

        Cursor cursor = sqLiteDatabase.rawQuery(queryStation, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getAllPairStation Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getAllPairStation: No item in PairStation Table");
        }

        while (!cursor.isAfterLast()) {
            pairStationArrayList.add(
                    new PairStation(
                            cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_START_STATION)),
                            cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_END_STATION)),
                            Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_IS_FIRST))),
                            cursor.getLong(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_TIMESTAMP))
                    ));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        return pairStationArrayList;
    }

    @Override
    public long deleteAll() {
        return 0;
    }
}
