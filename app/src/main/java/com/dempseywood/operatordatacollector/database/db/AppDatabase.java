package com.dempseywood.operatordatacollector.database.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.dempseywood.operatordatacollector.database.db.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;


/**
 * Created by musing on 05/09/2017.
 */


    @Database(entities = {EquipmentStatus.class}, version = 2)
    @TypeConverters({Converters.class})
    public abstract class AppDatabase extends RoomDatabase {
        public abstract EquipmentStatusDao equipmentStatusDao();
    }

