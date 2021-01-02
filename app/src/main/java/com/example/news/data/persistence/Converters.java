package com.example.news.data.persistence;

import androidx.room.TypeConverter;

import com.example.news.models.Source;


public class Converters {

    @TypeConverter
    public String fromSource(Source source) {
        return source.getName();
    }

    @TypeConverter
    public Source toSource(String name) {
        return new Source(name);
    }
}
