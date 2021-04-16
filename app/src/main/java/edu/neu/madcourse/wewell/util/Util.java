package edu.neu.madcourse.wewell.util;

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
        Date d = new Date(date);
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(d);
        return formattedDate;
    }

    public static String formatDateV2(long date) {
        Date d = new Date(date);
        Format format = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDate = format.format(d);
        return formattedDate;
    }




}
