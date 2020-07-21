package com.mobi.core.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobi.core.CoreSession;
import com.mobi.core.db.use.AnalysisTable;
import com.mobi.core.db.use.PushEventTable;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/9 15:09
 * @Dec 略
 */
public class AdProvider extends ContentProvider {

    private DbOpenHelper mDbOpenHelper;
    private static UriMatcher MATCHER;

    public static final String TAG = "AdProvider";
    private final Object mLock = new Object();

    //Uri info
    //authority
    public static String PEOPLE_INFO_AUTHORITY = "";
    public static Uri AUTHORITY_URI = null;
    //code
    private static final int PEOPLE_INFO_CODE = 1;
    private static final int PEOPLE_INFO_CODE2 = 2;

    @Override
    public boolean onCreate() {
        LogUtils.e(TAG, " onCreate ");
        mDbOpenHelper = new DbOpenHelper(getContext());
        CoreSession.get().init(getContext());

        initStaticValue();

        return true;
    }

    private void initStaticValue() {
        PEOPLE_INFO_AUTHORITY = getContext().getPackageName() + ".db.AdProvider";
        AUTHORITY_URI = Uri.parse("content://" + PEOPLE_INFO_AUTHORITY);
        //延迟创建一下这个MATCHER
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(PEOPLE_INFO_AUTHORITY, AnalysisTable.TABLE_NAME, PEOPLE_INFO_CODE);
        MATCHER.addURI(PEOPLE_INFO_AUTHORITY, PushEventTable.TABLE_NAME, PEOPLE_INFO_CODE2);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        LogUtils.e(TAG, " query ");
        try {
            synchronized (mLock) {
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
                switch (MATCHER.match(uri)) {
                    case PEOPLE_INFO_CODE:
                        return db.query(AnalysisTable.TABLE_NAME, projection, selection, selectionArgs,
                                null, null, sortOrder);
                    case PEOPLE_INFO_CODE2:
                        return db.query(PushEventTable.TABLE_NAME, projection, selection, selectionArgs,
                                null, null, sortOrder);
                    default:
                        LogUtils.e(TAG, " query 匹配失败" + uri);
                        break;
                }
            }
        } catch (SQLException e) {
            LogUtils.e(TAG, "query error:" + e.getMessage());
            return null;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues values) {

        LogUtils.e(TAG, " insert ");
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case PEOPLE_INFO_CODE: {
                // 特别说一下第二个参数是当name字段为空时，将自动插入一个NULL。
                long rowid = db.insert(AnalysisTable.TABLE_NAME, null, values);
                Uri insertUri = ContentUris.withAppendedId(uri, rowid);// 得到代表新增记录的Uri
                if (getContext() == null) {
                    Context context = CoreSession.get().getContext();
                    if (context != null) {
                        context.getContentResolver().notifyChange(uri, null);
                    }
                } else {
                    this.getContext().getContentResolver().notifyChange(uri, null);
                }
                return insertUri;
            }

            case PEOPLE_INFO_CODE2: {
                // 特别说一下第二个参数是当name字段为空时，将自动插入一个NULL。
                long rowid = db.insert(PushEventTable.TABLE_NAME, null, values);
                Uri insertUri = ContentUris.withAppendedId(uri, rowid);// 得到代表新增记录的Uri
                if (getContext() == null) {
                    Context context = CoreSession.get().getContext();
                    if (context != null) {
                        context.getContentResolver().notifyChange(uri, null);
                    }
                } else {
                    this.getContext().getContentResolver().notifyChange(uri, null);
                }
                return insertUri;
            }

            default:
                LogUtils.e(TAG, " insert 匹配失败");
                break;
//                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        LogUtils.e(TAG, " delete ");

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (MATCHER.match(uri)) {
            case PEOPLE_INFO_CODE:
                count = db.delete(AnalysisTable.TABLE_NAME, selection, selectionArgs);
                return count;
            case PEOPLE_INFO_CODE2:
                count = db.delete(PushEventTable.TABLE_NAME, selection, selectionArgs);
                return count;
            default:
                LogUtils.e(TAG, "delete 匹配失败 " + uri.toString());
                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        LogUtils.e(TAG, " update ");
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (MATCHER.match(uri)) {
            case PEOPLE_INFO_CODE:
                count = db.update(AnalysisTable.TABLE_NAME, values, selection, selectionArgs);
                return count;
            case PEOPLE_INFO_CODE2:
                count = db.update(PushEventTable.TABLE_NAME, values, selection, selectionArgs);
                return count;
            default:
                LogUtils.e(TAG, "update 匹配失败 " + uri.toString());
                break;
        }
        return 0;
    }
}
