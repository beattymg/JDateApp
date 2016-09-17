package com.android.mbeatty.jdwidget;

/**
* Created by MBeatty on 12/24/14.
*/

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class JulianDateApplication extends Activity {

    Handler textUpdate = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_julian_date_application);

        final TextView titleText = (TextView) findViewById(R.id.titleText);
        final TextView resultText = (TextView) findViewById(R.id.resultText);
        final TextView currentJulianDateText = (TextView) findViewById(R.id.currentJulianDateText);
        final TextView currentJulianDateTextResult = (TextView) findViewById(R.id.currentJulianDateTextResult);
        final Button convertButton = (Button) findViewById(R.id.convertButton);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        textUpdate.post(new Runnable(){

            @Override
            public void run() {
                double julianDate = calculateCurrentJulianDate();
                DecimalFormat df2 = new DecimalFormat( "#########0.00000" );
                double jDateFormatted = new Double(df2.format(julianDate)).doubleValue();
                currentJulianDateTextResult.setText(String.valueOf(jDateFormatted));
                textUpdate.postDelayed(this, 1000);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View view){
                double julianDate = calculateJulianDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                DecimalFormat df2 = new DecimalFormat( "#########0.00000" );
                double jDateFormatted = new Double(df2.format(julianDate)).doubleValue();

                resultText.setText(String.valueOf(jDateFormatted));
            }
        };
        convertButton.setOnClickListener(listener);

    }


    public double calculateJulianDate(int year, int month, int dayOfMonth)
    {
        double julianDate = Time.getJulianDay((new Date(year, month, dayOfMonth, 0, 0)).getTime(), 0);
        Calendar c = Calendar.getInstance();
        double seconds = (double)c.get(Calendar.SECOND);
        double minutes = (double)c.get(Calendar.MINUTE) + seconds/60;
        double hours = (double)c.get(Calendar.HOUR_OF_DAY) + minutes/60;

        double decDay = hours/24;
        julianDate += decDay;

        return julianDate;
    }

    public double calculateCurrentJulianDate()
    {
        double julianDate = Time.getJulianDay((new Date()).getTime(), 0);
        Calendar c = Calendar.getInstance();
        double seconds = (double)c.get(Calendar.SECOND);
        double minutes = (double)c.get(Calendar.MINUTE) + seconds/60;
        double hours = (double)c.get(Calendar.HOUR_OF_DAY) + minutes/60;

        double decDay = hours/24;
        julianDate += decDay;

        return julianDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.julian_date_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
