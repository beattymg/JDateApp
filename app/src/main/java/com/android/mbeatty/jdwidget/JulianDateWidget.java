package com.android.mbeatty.jdwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class JulianDateWidget extends AppWidgetProvider {

    public static final String LOG_TAG = "JDWidget";
    public static String CLOCK_WIDGET_UPDATE = "com.example.mbeatty.jdwidget.8BITCLOCK_WIDGET_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, JulianDateApplication.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.julian_date_widget);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        //double julianDate = calculateCurrentJulianDate();
        DecimalFormat df2 = new DecimalFormat( "#########0.00000" );
        double jDateFormatted = new Double(df2.format(calculateCurrentJulianDate())).doubleValue();

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        CharSequence julianDate = String.valueOf(jDateFormatted);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.julian_date_widget);
        views.setTextViewText(R.id.appwidget_text, julianDate);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public static double calculateCurrentJulianDate()
    {
        double julianDate = Time.getJulianDay((new Date()).getTime(), 0);
        System.out.println(julianDate);
        Calendar c = Calendar.getInstance();
        double seconds = (double)c.get(Calendar.SECOND);
        System.out.println(seconds);
        double minutes = (double)c.get(Calendar.MINUTE) + seconds/60;
        System.out.println(minutes);
        double hours = (double)c.get(Calendar.HOUR_OF_DAY) + minutes/60;
        System.out.println(hours);

        double decDay = hours/24;
        System.out.println(decDay);
        julianDate += decDay;
        System.out.println(julianDate);

        return julianDate;
    }

    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "Widget Provider enabled.  Starting timer to update widget every second");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000
                , createClockTickIntent(context));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "Widget Provider disabled. Turning off timer");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
    }

    @Override    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "Received intent " + intent);
        if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
            Log.d(LOG_TAG, "Clock update");
            // Get the widget manager and ids for this widget provider, then call the shared
            // clock update method.
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID: ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }
}
