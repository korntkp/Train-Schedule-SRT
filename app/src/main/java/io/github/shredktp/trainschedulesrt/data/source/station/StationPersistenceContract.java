package io.github.shredktp.trainschedulesrt.data.source.station;

import android.provider.BaseColumns;

/**
 * Created by Korshreddern on 08-Feb-17.
 */

public final class StationPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private StationPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class StationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Station";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LINE = "line";
    }
}
