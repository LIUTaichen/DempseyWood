package com.dempseywood.operatordatacollector.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.dempseywood.operatordatacollector.models.EquipmentStatus;

import java.util.Date;
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

    @Insert
    void insertAll(EquipmentStatus... equipmentStatuses);

    @Delete
    void delete(EquipmentStatus equipmentStatus);

    @Query("DELETE  FROM equipmentstatus")
    void deleteAll();

    @Query("SELECT * FROM equipmentstatus WHERE isSent == 0 ")
    List<EquipmentStatus> getAllNotSent();

    @Update
    void updateAll(List<EquipmentStatus> statusList);

    @Query("SELECT * FROM equipmentstatus ORDER BY timestamp DESC LIMIT 1")
    EquipmentStatus getLatestStatus();

    @Query("SELECT * FROM equipmentstatus WHERE timestamp > (:startTime)")
    List<EquipmentStatus> loadAllAfter(Date startTime);
}
