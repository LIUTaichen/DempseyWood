package com.dempseywood.operatordatacollector.helpers;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Jason.Liu on 15/01/2018.
 */
public class DateTimeHelperTest {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    @Test
    public void getTimeOfStartOfDay() throws Exception {


        System.out.println(DateTimeHelper.getTimeOfStartOfDay( formatter.parse("2017-10-05T15:23:01")));
        assertEquals(true, DateTimeHelper.getTimeOfStartOfDay( formatter.parse("2017-10-05T15:23:01")).equals(formatter.parse("2017-10-05T00:00:00")));
        assertEquals(true, DateTimeHelper.getTimeOfStartOfDay(formatter.parse("2017-10-05T00:00:00")).equals(formatter.parse("2017-10-05T00:00:00")));
        assertEquals(true, DateTimeHelper.getTimeOfStartOfDay(formatter.parse("2017-10-05T12:00:00")).equals(formatter.parse("2017-10-05T00:00:00")));



    }

}