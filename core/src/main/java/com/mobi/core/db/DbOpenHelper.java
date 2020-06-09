package com.mobi.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.mobi.core.db.use.AnalysisTable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/9 14:59
 * @Dec 数据库
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    //数据库版本号
    public static final int DB_VERSION = 1;
    //数据库名字
    public static final String DB_NAME = "mobi_ad_sdk.db";

    public DbOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AnalysisTable.CREATE_ANALYSIS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级版本的时候调用
    }
}
