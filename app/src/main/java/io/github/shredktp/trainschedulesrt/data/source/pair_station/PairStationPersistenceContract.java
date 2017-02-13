package io.github.shredktp.trainschedulesrt.data.source.pair_station;

import android.provider.BaseColumns;

/**
 * Created by Korshreddern on 11-Feb-17.
 */

public class PairStationPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PairStationPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PairStationEntry implements BaseColumns {
        public static final String TABLE_NAME = "PairStation";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_START_STATION = "startStation";
        public static final String COLUMN_NAME_END_STATION = "endStation";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_IS_FIRST = "isFirst";
    }
}
