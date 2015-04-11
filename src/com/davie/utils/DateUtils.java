package com.davie.utils;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: davie
 * Date: 15-4-10
 */
public class DateUtils {

    public static int [] getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int [] date = new int[]{year,month+1,day};
        return date;
    }

    public static int[] getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int mis = calendar.get(Calendar.MILLISECOND);
        int [] date = new int[]{hour,min,mis};
        return date;
    }
}
