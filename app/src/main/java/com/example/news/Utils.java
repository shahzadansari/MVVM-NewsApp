package com.example.news;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#ffeead")),
                    new ColorDrawable(Color.parseColor("#93cfb3")),
                    new ColorDrawable(Color.parseColor("#fd7a7a")),
                    new ColorDrawable(Color.parseColor("#faca5f")),
                    new ColorDrawable(Color.parseColor("#1ba798")),
                    new ColorDrawable(Color.parseColor("#6aa9ae")),
                    new ColorDrawable(Color.parseColor("#ffbf27")),
                    new ColorDrawable(Color.parseColor("#d93947"))
            };

    public static ColorDrawable getRandomDrawableColor() {
        int randomNumber = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[randomNumber];
    }

    public static String DateToTimeFormat(String existingStringDate){

        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));

        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = simpleDateFormat.parse(existingStringDate);
            time = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String DateFormat(String existingStringDate){
        String newDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(existingStringDate);
            newDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = existingStringDate;
        }

        return newDate;
    }

    public static String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

}
