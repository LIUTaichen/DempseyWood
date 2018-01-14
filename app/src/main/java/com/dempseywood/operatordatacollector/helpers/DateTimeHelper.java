package com.dempseywood.operatordatacollector.helpers;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by musing on 27/09/2017.
 */

public class DateTimeHelper {
    public static Date getTimeOfStartOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND ,0);
        calendar.set(Calendar.MILLISECOND , 0);
        Date timeOfStartOfToday = calendar.getTime();

        return timeOfStartOfToday;
    }
}
