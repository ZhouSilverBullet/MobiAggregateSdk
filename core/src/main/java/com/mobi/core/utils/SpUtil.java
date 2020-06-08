package com.mobi.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobi.core.CoreSession;

import java.util.Map;

public class SpUtil {
    private static final String SP_NAME = "mobi_aggregate_config";

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSp() {
        return CoreSession.get().getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 可以放子线程初始化
     * 因为本身里面就有锁，保证了，获取文件的过程
     */
    public static void initSp(Context context) {
        getSp(context);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        applyEditor(editor);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return defaultValue;
        }
        return sp.getLong(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        applyEditor(editor);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return defaultValue;
        }
        return sp.getInt(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        applyEditor(editor);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return "";
        }
        return sp.getString(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        applyEditor(editor);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return false;
        }
        return sp.getBoolean(key, defaultValue);
    }

    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return null;
        }

        return sp.getAll();
    }

    public static void remove(Context context, String key) {
        SharedPreferences sp = getSp(context);
        if (sp == null) {
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        applyEditor(editor);
    }

    private static void applyEditor(SharedPreferences.Editor editor) {
        editor.apply();
    }

    private static void commitEditor(SharedPreferences.Editor editor) {
        editor.commit();
    }

    public static void putLong(String key, long value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        applyEditor(editor);
    }

    public static void putLongCommit(String key, long value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        commitEditor(editor);
    }

    public static long getLong(String key, long defaultValue) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return defaultValue;
        }
        return sp.getLong(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        applyEditor(editor);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return defaultValue;
        }
        return sp.getInt(key, defaultValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        applyEditor(editor);
    }

    public static void putCommitString(String key, String value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        commitEditor(editor);
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return defaultValue;
        }
        return sp.getString(key, defaultValue);
    }

    public static String getString(String key) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return "";
        }
        return sp.getString(key, "");
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        applyEditor(editor);
    }

    public static void putBooleanCommit(String key, boolean value) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        commitEditor(editor);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return false;
        }
        return sp.getBoolean(key, defaultValue);
    }

    public static Map<String, ?> getAll() {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return null;
        }

        return sp.getAll();
    }

    public static void remove(String key) {
        SharedPreferences sp = getSp();
        if (sp == null) {
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        applyEditor(editor);
    }

}
