package com.dempseywood.operatordatacollector.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.dempseywood.operatordatacollector.data.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.data.dao.TaskDao;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.Task;


/**
 * Created by musing on 05/09/2017.
 */


    @Database(entities = {EquipmentStatus.class, Equipment.class, Task.class, Haul.class}, version = 4)
    @TypeConverters({Converters.class})
    public abstract class AppDatabase extends RoomDatabase {
        public abstract EquipmentStatusDao equipmentStatusDao();
        public abstract EquipmentDao equipmentDao();
        public abstract TaskDao taskDao();
        public abstract HaulDao haulDao();
    }

