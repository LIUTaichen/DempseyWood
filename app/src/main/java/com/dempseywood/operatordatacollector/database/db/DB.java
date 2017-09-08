package com.dempseywood.operatordatacollector.database.db;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by musing on 05/09/2017.
 */

public class DB {
    private static AppDatabase instance;
    private Context context;
    public static AppDatabase getInstance(){
        if(instance == null){
            throw new IllegalStateException("init() must be called before calling getInstance()");
        }
        return instance;
    }

    public static void init(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "dw-database").build();
        }
    }




}
