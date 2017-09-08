package com.dempseywood.operatordatacollector.database.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;

import java.util.List;

/**
 * Created by musing on 05/09/2017.
 */
@Dao
public interface EquipmentStatusDao {

    @Query("SELECT * FROM equipmentstatus")
    List<EquipmentStatus> getAll();

    @Query("SELECT * FROM equipmentstatus WHERE id IN (:ids)")
    List<EquipmentStatus> loadAllByIds(int[] ids);

    /*@Query("SELECT * FROM equipmentstatus WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    EquipmentStatusOld findByName(String first, String last);*/

    @Insert
    void insertAll(EquipmentStatus... equipmentStatuses);

    @Delete
    void delete(EquipmentStatus equipmentStatus);

    @Query("DELETE  FROM equipmentstatus")
    void deleteAll();
}
