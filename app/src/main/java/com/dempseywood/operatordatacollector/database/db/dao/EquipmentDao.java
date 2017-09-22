package com.dempseywood.operatordatacollector.database.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dempseywood.operatordatacollector.database.db.entity.Equipment;
import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;

import java.util.List;

/**
 * Created by musing on 22/09/2017.
 */
@Dao
public interface EquipmentDao {

    @Query("SELECT * FROM equipment")
    List<Equipment> getAll();


    @Insert
    void insertAll(Equipment... equipments);

    @Delete
    void delete(Equipment equipment);

    @Query("DELETE  FROM equipment")
    void deleteAll();

    @Query("SELECT *  FROM equipment WHERE NAME= (:name) LIMIT 1")
    Equipment findByName(String name);


}
