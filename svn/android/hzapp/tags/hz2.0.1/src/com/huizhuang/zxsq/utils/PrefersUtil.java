package com.huizhuang.zxsq.utils;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;

public class PrefersUtil {
	
	public static final String PREFERENCES_FILE = AppConfig.APP_NAME;

	private static PrefersUtil PREFERSUTIL;
    private static SharedPreferences cache;
    private SharedPreferences.Editor editor;

    private PrefersUtil(Context fra_act) {
        cache = fra_act.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        editor = cache.edit();
    }

    public static PrefersUtil getInstance() {
        if (PREFERSUTIL == null) {
            PREFERSUTIL = new PrefersUtil(ZxsqApplication.getInstance());
        }
        return PREFERSUTIL;
    }

    public String getStrValue(String key) {
        return cache.getString(key, "");
    }

    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setFolatValue(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloatValue(String key) {
        return cache.getFloat(key, 0);
    }

    public void setLongValue(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLongValue(String key) {
        return cache.getLong(key, 0);
    }

    public void removeSpf(String key) {
        cache =ZxsqApplication.getInstance().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        cache.edit().remove(key).commit();
    }

    public int getIntValue(String key, int defaultInt) {
        return cache.getInt(key, defaultInt);
    }

    public void setValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public Boolean getBooleanValue(String key, Boolean defaultBoolean) {
        return cache.getBoolean(key, defaultBoolean);
    }

    public void setValue(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    
    public void clearData(){
    	editor.clear();
    	editor.commit();
    }

    @SuppressLint("NewApi")
    public static void saveStringList(Context context, Set<String> value, String key) {
        cache = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        Editor editor = cache.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    @SuppressLint("NewApi")
    public static Set<String> getSavedStringList(Context context, String key) {
        cache = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return cache.getStringSet(key, null);
    }

}
