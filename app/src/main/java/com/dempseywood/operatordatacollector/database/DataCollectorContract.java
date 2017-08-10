package com.dempseywood.operatordatacollector.database;

import android.provider.BaseColumns;

import java.util.Date;

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

    public static class EquipmentStatusTO implements BaseColumns{
        public static final String TABLE_NAME = "equipment_status";
        public static final String COLUMN_NAME_EQUIPMENT = "equipment";
        public static final String COLUMN_NAME_OPERATOR = "operator";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";


    }

   // public static class

    public static final String SQL_CREATE_MACHINE =
            "CREATE TABLE IF NOT EXISTS " + MachineTO.TABLE_NAME + " (" +
                    MachineTO.COLUMN_NAME_PLATE_NO + " TEXT," +
                    MachineTO._ID + " INTEGER PRIMARY KEY," +
                    MachineTO.COLUMN_NAME_DESCRIPTION + " TEXT)";


    public static final String SQL_CREATE_EQUIPMENT_STATUS =
            "CREATE TABLE IF NOT EXISTS " + EquipmentStatusTO.TABLE_NAME + " (" +
                    EquipmentStatusTO._ID + " INTEGER PRIMARY KEY," +
                    EquipmentStatusTO.COLUMN_NAME_EQUIPMENT + " TEXT," +
                    EquipmentStatusTO.COLUMN_NAME_OPERATOR + " TEXT," +
                    EquipmentStatusTO.COLUMN_NAME_STATUS + " TEXT," +
                    EquipmentStatusTO.COLUMN_NAME_TASK + " TEXT," +
                    EquipmentStatusTO.COLUMN_NAME_TIMESTAMP + " LONG)";

    public static final String SQL_DELETE_MACHINE =
            "DROP TABLE IF EXISTS " + MachineTO.TABLE_NAME ;
    public static final String SQL_DELETE_EQUIPMENT_STATUS =
            "DROP TABLE IF EXISTS " + EquipmentStatusTO.TABLE_NAME ;



}
