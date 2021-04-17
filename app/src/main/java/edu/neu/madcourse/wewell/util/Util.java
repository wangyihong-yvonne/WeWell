package edu.neu.madcourse.wewell.util;

import android.icu.util.Calendar;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String formatTime(long time) {
        long minutes = time / 60;
        int seconds = (int) (time % 60);
        String formattedTime = minutes + "'" + seconds + "''";
        return formattedTime;
    }

    public static String formatDate(long date) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(date);
        Calendar now = Calendar.getInstance();
        Date d = new Date(date);
        Format format = new SimpleDateFormat("MMMM dd, HH:mm");
        String formattedDate = format.format(d);
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today, " + formattedDate;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday, " + formattedDate;
        }
        return formattedDate;
    }

    public static String formatDateV2(long date) {
        Date d = new Date(date);
        Format format = new SimpleDateFormat("MM-dd");
        String formattedDate = format.format(d);
        return formattedDate;
    }


}
