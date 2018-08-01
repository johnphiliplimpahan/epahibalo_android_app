package com.app.epahibalo.e_pahibalo.Helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateParser {

    private String mysqlDatePattern = "yyyy-MM-dd'T'HH:mm'Z'";

    public String convertDate(String mysqlDate){

        String[] date = mysqlDate.split("T");
        String parsedDate = "";

        try {
            Date dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(mysqlDate);
            Calendar c = Calendar.getInstance();
            c.setTime(dateString);
            c.add(Calendar.DATE,1);
            Date mysqlDateString = c.getTime();
            parsedDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(mysqlDateString);
            Log.d("HAHA: ",parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("HAHA: ",parsedDate);
        return parsedDate.equals("") ? mysqlDate : parsedDate;
    }

    public String convertTime(String mysqlTime){
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            Date dateObj = sdf.parse(mysqlTime);
            time = new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}