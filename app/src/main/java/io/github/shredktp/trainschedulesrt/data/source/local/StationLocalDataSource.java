package io.github.shredktp.trainschedulesrt.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.Contextor;
import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.source.StationDataSource;

/**
 * Created by Korshreddern on 28-Jan-17.
 */

public class StationLocalDataSource implements StationDataSource {
    private static final String TAG = "StationDSrc";
    private Context context;
    private static StationLocalDataSource instance;

    public static StationLocalDataSource getInstance() {
        if (instance == null) instance = new StationLocalDataSource();
        return instance;
    }

    private StationLocalDataSource() {
        this.context = Contextor.getInstance().getContext();
    }

    @Override
    public int countStation() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String queryStation = String.format("SELECT * FROM %s",
                Station.STATION_TABLE_NAME);

        Cursor cursor = sqLiteDatabase.rawQuery(queryStation, null);
        cursor.moveToFirst();

        int countStation = cursor.getCount();

        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return countStation;
    }

    @Override
    public long addStation(String name) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Station.Column.NAME, name);
        contentValues.put(Station.Column.LINE, "");
        long result = sqLiteDatabase.insert(Station.STATION_TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        dbHelper.close();
        return result;
    }

    @Override
    public long addStation(String name, String line) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Station.Column.NAME, name);
        contentValues.put(Station.Column.LINE, line);
        long result = sqLiteDatabase.insert(Station.STATION_TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        dbHelper.close();
        return result;
    }

    @Override
    public long addStation(ArrayList<Station> stationArrayList) {
        DbHelper dbHelper = new DbHelper(context);
//        Log.d(TAG, "addStation: after dbhelper");
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//        Log.d(TAG, "addStation: after getWrite" + stationArrayList.size());
        long result = 0;
        for (int i = 0; i < stationArrayList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Station.Column.NAME, stationArrayList.get(i).getName());
            contentValues.put(Station.Column.LINE, stationArrayList.get(i).getLine());
            result += sqLiteDatabase.insert(Station.STATION_TABLE_NAME, null, contentValues);
//            sqLiteDatabase.insert(Station.STATION_TABLE_NAME, null, contentValues);
        }
//        Log.d(TAG, "addStation: after insert: " + result);

        sqLiteDatabase.close();
        dbHelper.close();
        return result;
    }

    @Override
    public long addStation(Station[] station) {
        DbHelper dbHelper = new DbHelper(context);
//        Log.d(TAG, "addStation: after dbhelper");
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//        Log.d(TAG, "addStation: after getWrite" + stationArrayList.size());
        long result = 0;
        for (Station aStation : station) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Station.Column.NAME, aStation.getName());
            contentValues.put(Station.Column.LINE, aStation.getLine());
            result += sqLiteDatabase.insert(Station.STATION_TABLE_NAME, null, contentValues);
//            sqLiteDatabase.insert(Station.STATION_TABLE_NAME, null, contentValues);
        }
//        Log.d(TAG, "addStation: after insert: " + result);

        sqLiteDatabase.close();
        dbHelper.close();
        return result;
    }

    @Override
    public ArrayList<Station> getAllStation() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<Station> stationArrayList = new ArrayList<>();

        String queryStation = String.format("SELECT * FROM %s",
                Station.STATION_TABLE_NAME);

        Cursor cursor = sqLiteDatabase.rawQuery(queryStation, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getAllStation Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getAllStation: No item in Station Table");
        }

        while (!cursor.isAfterLast()) {
            stationArrayList.add(new Station(cursor.getString(cursor.getColumnIndex(Station.Column.NAME)), cursor.getString(cursor.getColumnIndex(Station.Column.LINE))));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
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
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<Station> stationArrayList = new ArrayList<>();

        String[] columns = new String[]{Station.Column.NAME, Station.Column.LINE};
        String selection = Station.Column.NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + piecesOfStation + "%"};
        Cursor cursor = sqLiteDatabase.query(Station.STATION_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "Search Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getAllStation: No item in Station Table");
        }

        while (!cursor.isAfterLast()) {
            stationArrayList.add(new Station(cursor.getString(cursor.getColumnIndex(Station.Column.NAME)), cursor.getString(cursor.getColumnIndex(Station.Column.LINE))));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return stationArrayList;
    }
}
