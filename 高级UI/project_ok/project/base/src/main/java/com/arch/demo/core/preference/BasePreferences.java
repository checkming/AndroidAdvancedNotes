package com.arch.demo.core.preference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.arch.demo.core.utils.EditorUtils;
import com.arch.demo.core.utils.Utils;

import java.util.Map;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
abstract class BasePreferences {
    protected static Application sApplication;

    protected SharedPreferences mPreference;

    public BasePreferences() {
        if (Utils.isEmpty(getSPFileName())) {
            mPreference = PreferenceManager.getDefaultSharedPreferences(sApplication);
        } else {
            mPreference = sApplication.getSharedPreferences(getSPFileName(), Context.MODE_PRIVATE);
        }
    }

    protected abstract String getSPFileName();

    public String getString(String key, String defValue) {
        return mPreference.getString(key, defValue);
    }

    public Boolean getBoolean(String key, boolean defBool) {
        return mPreference.getBoolean(key, defBool);
    }

    public void setBoolean(String key, boolean bool) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(key, bool);
        EditorUtils.fastCommit(editor);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(key, value);
        EditorUtils.fastCommit(editor);
    }

    public long getLong(String key, long defValue) {
        return mPreference.getLong(key, defValue);
    }

    public String getString(String key) {
        return mPreference.getString(key, "");
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key, value);
        EditorUtils.fastCommit(editor);
    }

    public int getInt(String key, int defaultVal) {
        return mPreference.getInt(key, defaultVal);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(key, value);
        EditorUtils.fastCommit(editor);
    }

    public void remove(String key) {
        if (!TextUtils.isEmpty(key) && mPreference.contains(key)) {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.remove(key);
            EditorUtils.fastCommit(editor);
        }
    }

    public boolean contains(String key) {
        return mPreference.contains(key);
    }

    public void clearAll() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.clear();
        EditorUtils.fastCommit(editor);
    }

    public Map<String, ?> getAll() {
        return mPreference.getAll();
    }
}
