package com.mcc.tv.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtilities {

    private static Date dt = null;

    public TimeUtilities(String programTime){
        try {
            dt = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a", Locale.US).parse(programTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getDateOnly(){
        return new SimpleDateFormat("dd MMMM", Locale.US).format(dt);
    }

    public static String getTimeOnly(){
        return new SimpleDateFormat("hh:mm a", Locale.US).format(dt);
    }
}
