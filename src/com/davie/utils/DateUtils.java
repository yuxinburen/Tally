package com.davie.utils;

import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: davie
 * Date: 15-4-10
 */
public class DateUtils {

    public static Date string2Date(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        try {
            Date date = simpleDateFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int [] getCurrentDate(){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int [] date = new int[]{year,month+1,day,dayOfWeek};
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
