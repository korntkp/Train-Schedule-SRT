package io.github.shredktp.trainschedulesrt.data.source.train_schedule;

import android.provider.BaseColumns;

/**
 * Created by Korshreddern on 10-Feb-17.
 */

public class TrainSchedulePersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TrainSchedulePersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TrainScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "TrainSchedule";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_START_STATION = "startStation";
        public static final String COLUMN_NAME_END_STATION = "endStation";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
    }
}
