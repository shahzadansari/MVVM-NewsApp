package com.example.news.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

//    public static final String API_KEY = "c2194f57d73e4392ae4ee0bf69e9d391";
//    public static final String API_KEY = "9be5527ccbff498c87db85f61a585e19";
    public static final String API_KEY = "7044e74d6d8d4aea990766aa30c2e8fd";


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

    public static String DateToTimeFormat(String existingStringDate) {

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

    public static String DateFormat(String existingStringDate) {
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

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        String country = locale.getLanguage();
        return country.toLowerCase();
    }

    public static boolean checkLocale(String locale) {
        boolean isAvailable = false;

        String[] availableLocales = {"ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn",
                "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in",
                "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl",
                "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us",
                "ve", "za"};

        for (String availableLocale : availableLocales) {
            if (availableLocale.equals(locale)) {
                return true;
            }
        }

        return isAvailable;
    }

    public static boolean checkLanguage(String language) {
        boolean isAvailable = false;

        String[] availableLanguages = {"ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt",
                "ru", "se", "ud", "zh"};

        for (String availableLanguage : availableLanguages) {
            if (availableLanguage.equals(language)) {
                return true;
            }
        }

        return isAvailable;
    }
}
