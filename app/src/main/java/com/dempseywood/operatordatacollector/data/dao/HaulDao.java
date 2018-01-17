package com.dempseywood.operatordatacollector.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

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
}
