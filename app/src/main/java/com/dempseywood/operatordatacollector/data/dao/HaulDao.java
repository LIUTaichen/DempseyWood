package com.dempseywood.operatordatacollector.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.Haul;

import java.util.Date;
import java.util.List;

/**
 * Created by Jason.Liu on 18/01/2018.
 */
@Dao
public interface HaulDao {
    @Query("SELECT * FROM haul WHERE loadTime > (:startTime)")
    List<Haul> loadAllAfter(Date startTime);

    @Insert
    void insertAll(Haul... hauls);

    @Insert
    void save(Haul haul);

    @Query("SELECT * FROM haul WHERE uuid == (:uuid) ")
    Haul findOneByUuid(String uuid);

    @Update
    void update(Haul haul);

    @Query("SELECT * FROM haul where uuid in (:uuids)")
    List<Haul> findAllWithUuidIn(List<String> uuids);
}
