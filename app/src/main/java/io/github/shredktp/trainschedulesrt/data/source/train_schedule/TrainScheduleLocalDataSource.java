package io.github.shredktp.trainschedulesrt.data.source.train_schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.TrainSchedule;
import io.github.shredktp.trainschedulesrt.data.source.DbHelper;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainSchedulePersistenceContract.TrainScheduleEntry;

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

    @Override
    public long add(TrainSchedule trainSchedule) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TrainScheduleEntry.COLUMN_NAME_STAR_END_STATION, trainSchedule.getStartEndStation());
        contentValues.put(TrainScheduleEntry.COLUMN_NAME_NUMBER, trainSchedule.getNumber());
        contentValues.put(TrainScheduleEntry.COLUMN_NAME_TYPE, trainSchedule.getType());
        contentValues.put(TrainScheduleEntry.COLUMN_NAME_START_TIME, trainSchedule.getStartTime());
        contentValues.put(TrainScheduleEntry.COLUMN_NAME_END_TIME, trainSchedule.getEndTime());
        long result = sqLiteDatabase.insert(TrainScheduleEntry.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return result;
    }

    @Override
    public long add(ArrayList<TrainSchedule> trainScheduleArrayList) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long result = 0;
        for (int i = 0; i < trainScheduleArrayList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TrainScheduleEntry.COLUMN_NAME_STAR_END_STATION, trainScheduleArrayList.get(i).getStartEndStation());
            contentValues.put(TrainScheduleEntry.COLUMN_NAME_NUMBER, trainScheduleArrayList.get(i).getNumber());
            contentValues.put(TrainScheduleEntry.COLUMN_NAME_TYPE, trainScheduleArrayList.get(i).getType());
            contentValues.put(TrainScheduleEntry.COLUMN_NAME_START_TIME, trainScheduleArrayList.get(i).getStartTime());
            contentValues.put(TrainScheduleEntry.COLUMN_NAME_END_TIME, trainScheduleArrayList.get(i).getEndTime());
            result += sqLiteDatabase.insert(TrainScheduleEntry.TABLE_NAME, null, contentValues);
        }
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public ArrayList<TrainSchedule> getTrainScheduleByStation(String startEndStation) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<TrainSchedule> trainScheduleArrayList = new ArrayList<>();

        String queryTrainSchedule = String.format("SELECT * FROM %s WHERE %s == %s",
                TrainScheduleEntry.TABLE_NAME,
                TrainScheduleEntry.COLUMN_NAME_STAR_END_STATION,
                startEndStation);

        Cursor cursor = sqLiteDatabase.rawQuery(queryTrainSchedule, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getTrainScheduleByStation Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getTrainScheduleByStation: No item in TrainSchedule Table");
        }

        while (!cursor.isAfterLast()) {
            trainScheduleArrayList.add(
                    new TrainSchedule(
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_STAR_END_STATION)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_TYPE)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_START_TIME)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_END_TIME))
                    ));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        return trainScheduleArrayList;
    }

    @Override
    public ArrayList<TrainSchedule> getTrainScheduleByIsFirst() {
        return null;
    }

    @Override
    public ArrayList<TrainSchedule> getAllTrainSchedule() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        ArrayList<TrainSchedule> trainScheduleArrayList = new ArrayList<>();

        String queryTrainSchedule = String.format("SELECT * FROM %s",
                TrainScheduleEntry.TABLE_NAME);

        Cursor cursor = sqLiteDatabase.rawQuery(queryTrainSchedule, null);
        cursor.moveToFirst();

        int countCursor = cursor.getCount();
        Log.d(TAG, "getAllTrainSchedule Row count: " + countCursor);

        if (countCursor == 0) {
            Log.w(TAG, "getAllTrainSchedule: No item in TrainSchedule Table");
        }

        while (!cursor.isAfterLast()) {
            trainScheduleArrayList.add(
                    new TrainSchedule(
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_STAR_END_STATION)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_TYPE)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_START_TIME)),
                            cursor.getString(cursor.getColumnIndex(TrainScheduleEntry.COLUMN_NAME_END_TIME))
                    ));
            cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();
        return trainScheduleArrayList;
    }

    @Override
    public long deleteAll() {
        return 0;
    }

    @Override
    public long deleteStar() {
        return 0;
    }
}
