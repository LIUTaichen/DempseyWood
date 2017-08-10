package com.dempseywood.operatordatacollector.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musing on 03/08/2017.
 */

public class DbHelper extends SQLiteOpenHelper {




    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "OperatorDataCollector.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataCollectorContract.SQL_CREATE_MACHINE);
        db.execSQL(DataCollectorContract.SQL_CREATE_EQUIPMENT_STATUS);
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        //noinspection TryFinallyCanBeTryWithResources not available with API < 19
        Log.i("DbHelper", "on create" );
        try {
            List<String> tables = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }

            for (String table : tables) {
                if (table.startsWith("sqlite_")) {
                    continue;
                }
                //db.execSQL("DROP TABLE IF EXISTS " + table);
                Log.e("DbHelper", " table after create" + table);
            }
        } finally {
            cursor.close();
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DataCollectorContract.SQL_DELETE_MACHINE);
        db.execSQL(DataCollectorContract.SQL_DELETE_EQUIPMENT_STATUS);

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        //noinspection TryFinallyCanBeTryWithResources not available with API < 19
        try {
            List<String> tables = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }

            for (String table : tables) {
                if (table.startsWith("sqlite_")) {
                    continue;
                }
                //db.execSQL("DROP TABLE IF EXISTS " + table);
                Log.e("DbHelper", " table after drop " + table);
            }
        } finally {
            cursor.close();
        }
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
