package com.coco.cocoweather.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Abhishek Singh on 23/4/19.
 */
public class Common {

    public static final String APP_ID = "ec73e3d98807feb25bb3e83bbbc2242a";

    public static Location current_location = null;

    public static String convertUnixToDate(Integer dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat =     new SimpleDateFormat("HH:mm dd EEE MM yyyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

    public static String convertUnixToHour(Integer sunset) {
        Date date = new Date(sunset*1000L);
        SimpleDateFormat simpleDateFormat =     new SimpleDateFormat("HH:mm");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }
}
