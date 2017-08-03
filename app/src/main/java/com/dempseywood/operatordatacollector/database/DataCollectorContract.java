package com.dempseywood.operatordatacollector.database;

import android.provider.BaseColumns;

/**
 * Created by musing on 03/08/2017.
 */

public final class DataCollectorContract {

    private DataCollectorContract(){

    }

    public static class MachineTO implements BaseColumns{
        public static final String TABLE_NAME = "machine";
        public static final String COLUMN_NAME_PLATE_NO = "plate_no";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MachineTO.TABLE_NAME + " (" +
                    MachineTO.COLUMN_NAME_PLATE_NO + " TEXT," +
                    MachineTO._ID + " INTEGER PRIMARY KEY," +
                    MachineTO.COLUMN_NAME_DESCRIPTION + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MachineTO.TABLE_NAME;

}
