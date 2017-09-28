package com.dempseywood.operatordatacollector.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by musing on 06/09/2017.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}