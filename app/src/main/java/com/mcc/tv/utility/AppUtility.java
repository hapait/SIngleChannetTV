package com.mcc.tv.utility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.mcc.tv.R;
import com.mcc.tv.receiver.AlarmReceiver;
import com.mcc.tv.model.ProgramTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtility {

    private static AppUtility appUtilities = null;

    // create single instance
    public static AppUtility getInstance() {
        if (appUtilities == null) {
            appUtilities = new AppUtility();
        }
        return appUtilities;
    }

    public static void setAlarm(Context context, String alarmTime, int alarmRequestCode) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarm_Test", "tvProgramNotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmRequestCode, alarmIntent, 0);

        long timeToTriggerAlarm = getTimeInMillis(alarmTime);
        if (timeToTriggerAlarm < System.currentTimeMillis()) {
            timeToTriggerAlarm += (24 * 60 * 60 * 1000);
        }

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeToTriggerAlarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, timeToTriggerAlarm, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
    public static void cancelAlarm(Context mContext, int alarmRequestCode) {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarmRequestCode, alarmIntent, 0);
        manager.cancel(pendingIntent);
    }
    private static long getTimeInMillis(String time){
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");
        Date date = null;
        try {
            date = datetimeFormatter1.parse(time); //"14.03.2018 5:40:00 pm"
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    public static ProgramTime getTime(String time){
        // "12.03.2018 5:40 pm Monday";
        String[] parts = time.split(" ");
        String dateMonthYear = parts[0]; // 12.03.2018
        String hourMinute = parts[1]; // 5:40
        String amPM = parts[2]; // pm
        String day = parts[3]; // Monday
        return new ProgramTime(dateMonthYear+" "+hourMinute+":00 "+amPM, hourMinute+" "+amPM, day);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public static void noInternetWarning(View view, final Context context) {
        if (!isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(context.getString(R.string.connect), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            snackbar.show();
        }
    }
    // common snack bar bellow different view and necessary message
    public static void showSnackBarMsg(Context context, View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void shareApp(Activity activity) {
        try {
            final String appPackageName = activity.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.share) + "https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rateThisApp(Activity activity) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
