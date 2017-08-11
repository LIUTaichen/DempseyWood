package com.dempseywood.operatordatacollector.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dempseywood.operatordatacollector.database.DataCollectorContract;
import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.operatordetail.Machine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musing on 10/08/2017.
 */

public class MachineDAO {
    private DbHelper dbHelper;

    public MachineDAO(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Machine> findAllMachines() {
        List<Machine> machines = new ArrayList<Machine>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DataCollectorContract.MachineTO._ID,
                DataCollectorContract.MachineTO.COLUMN_NAME_PLATE_NO,
                DataCollectorContract.MachineTO.COLUMN_NAME_DESCRIPTION
        };

        Cursor cursor = db.query(
                DataCollectorContract.MachineTO.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        // String[] machines = new String[ cursor.getCount()];
        while (cursor.moveToNext()) {
            Machine machine = new Machine();
            machine.setPlateNo(cursor.getString(cursor.getColumnIndexOrThrow(DataCollectorContract.MachineTO.COLUMN_NAME_PLATE_NO)));
            machine.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DataCollectorContract.MachineTO.COLUMN_NAME_DESCRIPTION)));
            machines.add(machine);
        }
        cursor.close();
        db.close();
        return machines;
    }

    public Long saveMachine( String plate, String desc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys

        ContentValues values = new ContentValues();
        values.put(DataCollectorContract.MachineTO.COLUMN_NAME_PLATE_NO, plate);
        values.put(DataCollectorContract.MachineTO.COLUMN_NAME_DESCRIPTION, desc);
// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DataCollectorContract.MachineTO.TABLE_NAME, null, values);
        db.close();
        return newRowId;

    }


    public void deleteMachine(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataCollectorContract.MachineTO.TABLE_NAME, "", new String[]{});
        db.close();
    }

    public void removeAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataCollectorContract.MachineTO.TABLE_NAME, "", new String[]{});
        db.close();
    }

    public void deleteRowFromTable( String columnName, String keyValue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = columnName + "=?";
        String[] whereArgs = new String[]{String.valueOf(keyValue)};
        db.delete(DataCollectorContract.MachineTO.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
}
