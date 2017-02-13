package io.github.shredktp.trainschedulesrt.data.source.station;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.source.DbHelper;

import io.github.shredktp.trainschedulesrt.data.source.station.StationPersistenceContract.StationEntry;

/**
 * Created by Korshreddern on 28-Jan-17.
 */

public class StationLocalDataSource implements StationDataSource {
    private static final String TAG = "StationDSrc";

    private static StationLocalDataSource INSTANCE;
    private DbHelper dbHelper;

    public static StationLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StationLocalDataSource(context);
        }
        return INSTANCE;
    }

    private StationLocalDataSource(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    @Override
    public int countStation() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String queryStation = String.format("SELECT * FROM %s",
                StationPersistenceContract.StationEntry.TABLE_NAME);

        Cursor cursor = sqLiteDatabase.rawQuery(queryStation, null);
        cursor.moveToFirst();

        int countStation = cursor.getCount();

        cursor.close();
        sqLiteDatabase.close();
        return countStation;
    }

    @Override
    public long addStation(String name) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(StationEntry.COLUMN_NAME_NAME, name);
        contentValues.put(StationEntry.COLUMN_NAME_LINE, "");
        long result = sqLiteDatabase.insert(StationEntry.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return result;
    }

    @Override
    public long addStation(String name, String line) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(StationEntry.COLUMN_NAME_NAME, name);
        contentValues.put(StationEntry.COLUMN_NAME_LINE, line);
        long result = sqLiteDatabase.insert(StationEntry.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return result;
    }

    @Override
    public long addStation(ArrayList<Station> stationArrayList) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long result = 0;
        for (int i = 0; i < stationArrayList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(StationEntry.COLUMN_NAME_NAME, stationArrayList.get(i).getName());
            contentValues.put(StationEntry.COLUMN_NAME_LINE, stationArrayList.get(i).getLine());
            result += sqLiteDatabase.insert(StationEntry.TABLE_NAME, null, contentValues);
        }
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public long addStation(Station[] station) {
//        Log.d(TAG, "addStation: after dbhelper");
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//        Log.d(TAG, "addStation: after getWrite" + stationArrayList.size());
        long result = 0;
        for (Station aStation : station) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(StationEntry.COLUMN_NAME_NAME, aStation.getName());
            contentValues.put(StationEntry.COLUMN_NAME_LINE, aStation.getLine());
            result += sqLiteDatabase.insert(StationEntry.TABLE_NAME, null, contentValues);
//            sqLiteDatabase.insert(StationPersistenceContract.StationEntry.TABLE_NAME, null, contentValues);
        }
//        Log.d(TAG, "addStation: after insert: " + result);

        sqLiteDatabase.close();
        return result;
    }

    @Override
    public ArrayList<Station> getAllStation() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<Station> stationArrayList = new ArrayList<>();

        String queryStation = String.format("SELECT * FROM %s",
                StationEntry.TABLE_NAME);

        Cursor cursor = sqLiteDatabase.rawQuery(queryStation, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getAllStation Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getAllStation: No item in Station Table");
        }

        while (!cursor.isAfterLast()) {
            stationArrayList.add(
                    new Station(
                            cursor.getString(cursor.getColumnIndex(StationEntry.COLUMN_NAME_NAME)),
                            cursor.getString(cursor.getColumnIndex(StationEntry.COLUMN_NAME_LINE))
                    ));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        return stationArrayList;
    }

    @Override
    public Station getStation(String name) {
        return null;
    }

    @Override
    public Station getStation(String name, String line) {
        return null;
    }

    @Override
    public ArrayList<Station> searchStation(String piecesOfStation) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<Station> stationArrayList = new ArrayList<>();

        String[] columns = new String[]{
                StationEntry.COLUMN_NAME_NAME,
                StationPersistenceContract.StationEntry.COLUMN_NAME_LINE};

        String selection = StationEntry.COLUMN_NAME_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + piecesOfStation + "%"};

        Cursor cursor = sqLiteDatabase.query(
                StationEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "Search Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getAllStation: No item in Station Table");
        }

        while (!cursor.isAfterLast()) {
            stationArrayList.add(new Station(
                    cursor.getString(cursor.getColumnIndex(StationEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(StationEntry.COLUMN_NAME_LINE))
            ));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        return stationArrayList;
    }
}
