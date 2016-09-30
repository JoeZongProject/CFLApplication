package com.joecorelibrary.base;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class BasePreferences {

    private SharedPreferences preferences;

    public void request(Context aContext, String spName){
        preferences = aContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public boolean setString(final String entry, String value) {
        return preferences.edit().putString(entry, value).commit();
    }

    public boolean setBoolean(final String entry, boolean value) {
        return preferences.edit().putBoolean(entry, value).commit();
    }

    public boolean setInt(final String entry, int value) {
        return preferences.edit().putInt(entry, value).commit();
    }

    public boolean setLong(final String entry, long value) {
        return preferences.edit().putLong(entry, value).commit();
    }

    public boolean setFloat(final String entry, float value) {
        return preferences.edit().putFloat(entry, value).commit();
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public String getString(final String entry, String defaultValue) {
        return preferences.getString(entry, defaultValue);
    }

    public int getInt(final String entry, int defaultValue) {
        return preferences.getInt(entry, defaultValue);
    }

    public float getFloat(final String entry, float defaultValue) {
        return preferences.getFloat(entry, defaultValue);
    }

    public long getLong(final String entry, long defaultValue) {
        return preferences.getLong(entry, defaultValue);

    }

    public boolean getBoolean(final String entry, boolean defaultValue) {
        return preferences.getBoolean(entry, defaultValue);
    }

    public boolean contains(String entry) {
        return preferences.contains(entry);
    }

    public boolean clear(String entry) {
        return preferences.edit().remove(entry).commit();
    }

    public boolean clearAll() {
        return preferences.edit().clear().commit();
    }



}
