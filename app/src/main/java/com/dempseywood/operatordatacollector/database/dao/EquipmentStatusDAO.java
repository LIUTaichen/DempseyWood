package com.dempseywood.operatordatacollector.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dempseywood.operatordatacollector.database.DataCollectorContract;
import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.rest.status.EquipmentStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by musing on 10/08/2017.
 */

public class EquipmentStatusDAO {
    private DbHelper dbHelper;

    public EquipmentStatusDAO(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<EquipmentStatus> findAllEquipmentStatus() {
        List<EquipmentStatus> equipmentStatusList = new ArrayList<EquipmentStatus>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DataCollectorContract.EquipmentStatusTO._ID,
                DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_EQUIPMENT,
                DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_OPERATOR,
                DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_TASK,
                DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_STATUS,
                DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_TIMESTAMP,
        };

        Cursor cursor = db.query(
                DataCollectorContract.EquipmentStatusTO.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        // String[] machines = new String[ cursor.getCount()];
        while (cursor.moveToNext()) {
            EquipmentStatus equipmentStatus = new EquipmentStatus();
            equipmentStatus.setEquipment(cursor.getString(cursor.getColumnIndexOrThrow(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_EQUIPMENT)));
            equipmentStatus.setOperator(cursor.getString(cursor.getColumnIndexOrThrow(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_OPERATOR)));
            equipmentStatus.setTask(cursor.getString(cursor.getColumnIndexOrThrow(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_TASK)));
            equipmentStatus.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_STATUS)));
            Long timeInEpoch = cursor.getLong(cursor.getColumnIndexOrThrow(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_TIMESTAMP));
            Date date = new Date(timeInEpoch);
            equipmentStatus.setTimestamp(date);

            equipmentStatusList.add(equipmentStatus);
        }
        cursor.close();
        return equipmentStatusList;
    }

    public Long saveEquipmentStatus( EquipmentStatus equipmentStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys

        ContentValues values = new ContentValues();
        values.put(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_EQUIPMENT, equipmentStatus.getEquipment());
        values.put(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_OPERATOR, equipmentStatus.getOperator());
        values.put(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_TASK, equipmentStatus.getTask());
        values.put(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_STATUS, equipmentStatus.getStatus());
        values.put(DataCollectorContract.EquipmentStatusTO.COLUMN_NAME_TIMESTAMP, equipmentStatus.getTimestamp().getTime());
// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DataCollectorContract.EquipmentStatusTO.TABLE_NAME, null, values);
        return newRowId;
    }



    public void removeAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataCollectorContract.EquipmentStatusTO.TABLE_NAME, "", new String[]{});
    }

    public void deleteRowFromTable( String columnName, String keyValue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = columnName + "=?";
        String[] whereArgs = new String[]{String.valueOf(keyValue)};
        db.delete(DataCollectorContract.EquipmentStatusTO.TABLE_NAME, whereClause, whereArgs);
    }


}
