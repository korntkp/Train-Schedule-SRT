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
    private static final int IS_IT_FIRST_TRUE = 1;
    private static final int IS_IT_FIRST_FALSE = 0;

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
        contentValues.put(PairStationEntry.COLUMN_NAME_COUNT, pairStation.getCount());
        contentValues.put(PairStationEntry.COLUMN_NAME_IS_FIRST, pairStation.isSeeItFirst());
        contentValues.put(PairStationEntry.COLUMN_NAME_TIMESTAMP, pairStation.getTimestamp());
        long result = sqLiteDatabase.insertWithOnConflict(PairStationEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        sqLiteDatabase.close();
        return result;
    }

    @Override
    public long updateSeeItFirst(PairStation pairStation) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        long clearSeeItFirstResult = clearSeeItFirst(sqLiteDatabase);
//        long clearSeeItFirstResult = deleteSeeItFirstPairStation();
        Log.d(TAG, "updateSeeItFirst clearSeeItFirstResult: " + clearSeeItFirstResult);

        ContentValues contentValues = new ContentValues();
        contentValues.put(PairStationEntry.COLUMN_NAME_IS_FIRST, pairStation.isSeeItFirst());

        String whereCause = PairStationEntry.COLUMN_NAME_START_STATION + " LIKE ? AND " +
                PairStationEntry.COLUMN_NAME_END_STATION + " LIKE ?";
        String[] whereArgs = {pairStation.getStartStation(), pairStation.getEndStation()};

        long result = sqLiteDatabase.update(PairStationEntry.TABLE_NAME, contentValues, whereCause, whereArgs);

        sqLiteDatabase.close();
        return result;
    }

    private long clearSeeItFirst(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "clearSeeItFirst: ");
        ContentValues contentValues = new ContentValues();
        contentValues.put(PairStationEntry.COLUMN_NAME_IS_FIRST, IS_IT_FIRST_FALSE);

        String whereCause = PairStationEntry.COLUMN_NAME_IS_FIRST + " LIKE ?";
        String[] whereArgs = {String.valueOf(IS_IT_FIRST_TRUE)};

        return sqLiteDatabase.update(PairStationEntry.TABLE_NAME, contentValues, whereCause, whereArgs);
    }

    @Override
    public PairStation getSeeFirstPairStation() throws Exception {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        String[] projection = {
                PairStationEntry.COLUMN_NAME_START_STATION,
                PairStationEntry.COLUMN_NAME_END_STATION,
                PairStationEntry.COLUMN_NAME_COUNT,
                PairStationEntry.COLUMN_NAME_TIMESTAMP,
                PairStationEntry.COLUMN_NAME_IS_FIRST,
        };

        String selection = PairStationEntry.COLUMN_NAME_IS_FIRST + " LIKE ?";
        String[] selectionArgs = {String.valueOf(IS_IT_FIRST_TRUE)};

        Cursor cursor = sqLiteDatabase.query(PairStationEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getSeeFirstPairStation Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getSeeFirstPairStation: No item in PairStation Table");
            throw new Exception("No item in PairStation Table");
        }

        PairStation pairStationResult = new PairStation(
                cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_START_STATION)),
                cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_END_STATION)),
                cursor.getInt(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_COUNT)),
                cursor.getInt(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_IS_FIRST)),
                cursor.getLong(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_TIMESTAMP))
        );

        cursor.close();
        sqLiteDatabase.close();
        return pairStationResult;
    }

    @Override
    public ArrayList<PairStation> getAllPairStation() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ArrayList<PairStation> pairStationArrayList = new ArrayList<>();

        String[] projection = {
                PairStationEntry.COLUMN_NAME_START_STATION,
                PairStationEntry.COLUMN_NAME_END_STATION,
                PairStationEntry.COLUMN_NAME_COUNT,
                PairStationEntry.COLUMN_NAME_TIMESTAMP,
                PairStationEntry.COLUMN_NAME_IS_FIRST,
        };

        Cursor cursor = sqLiteDatabase.query(PairStationEntry.TABLE_NAME, projection, null, null,
                null, null, null);
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
                            cursor.getInt(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_IS_FIRST)),
                            cursor.getLong(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_TIMESTAMP))
                    ));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        return pairStationArrayList;
    }

    @Override
    public boolean isSeeItFirstByStation(String startStation, String endStation) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        boolean result = false;

        String[] projection = {
                PairStationEntry.COLUMN_NAME_START_STATION,
                PairStationEntry.COLUMN_NAME_END_STATION,
                PairStationEntry.COLUMN_NAME_IS_FIRST,
        };

//        String selection = PairStationEntry.COLUMN_NAME_IS_FIRST + " LIKE ?";
        String selection = PairStationEntry.COLUMN_NAME_START_STATION + " LIKE ? AND " +
                PairStationEntry.COLUMN_NAME_END_STATION + " LIKE ?";
//        String[] selectionArgs = {String.valueOf(IS_IT_FIRST_TRUE)};
        String[] selectionArgs = {startStation, endStation};

        Cursor cursor = sqLiteDatabase.query(PairStationEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();

        if (countCursor <= 0) {
            Log.d(TAG, "isSeeItFirstByStation Row count: " + countCursor);
            result = false;
        } else {
//            String queryStartStation = cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_START_STATION));
//            String queryEndStation = cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_END_STATION));
//            Log.d(TAG, "isSeeItFirstByStation: " + queryStartStation);
//            Log.d(TAG, "isSeeItFirstByStation: " + queryEndStation);
            String isFirst = cursor.getString(cursor.getColumnIndex(PairStationEntry.COLUMN_NAME_IS_FIRST));
            Log.i(TAG, "isSeeItFirstByStation count cursor: " + countCursor);
            Log.i(TAG, "isSeeItFirstByStation isFirst: " + isFirst);
            if (isFirst.equals("1")) {
                result = true;
            }
        }

        Log.i(TAG, "isSeeItFirstByStation result: " + result);
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public int deleteAll() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(PairStationEntry.TABLE_NAME, null, null);
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public int deleteSeeItFirstPairStation() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Log.i(TAG, "deleteSeeItFirstPairStation: ");

        String whereClause = PairStationEntry.COLUMN_NAME_IS_FIRST + " LIKE ?";
        String[] whereArgs = {String.valueOf(IS_IT_FIRST_TRUE)};

        int result = sqLiteDatabase.delete(PairStationEntry.TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
        return result;
    }
}
