package com.dempseywood.operatordatacollector.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.Task;

import java.util.List;

/**
 * Created by Jason.Liu on 18/01/2018.
 */
@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();


    @Insert
    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);

    @Query("DELETE  FROM task")
    void deleteAll();

    @Query("SELECT *  FROM task WHERE NAME= (:name) LIMIT 1")
    Equipment findByName(String name);
}
