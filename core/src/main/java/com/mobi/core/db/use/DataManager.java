package com.mobi.core.db.use;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mobi.core.analysis.AnalysisBean;
import com.mobi.core.analysis.event.PushEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/9 15:43
 * @Dec 略
 */
public class DataManager {

    public static void addAnalysis(Context context, AnalysisBean bean) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = AnalysisTable.getContentUri();
        ContentValues contentValues = AnalysisTable.putValues(bean);
        contentResolver.insert(uri, contentValues);
    }

    public static void updateAnalysis(Context context, String posid, AnalysisBean bean) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = AnalysisTable.getContentUri();
        ContentValues contentValues = AnalysisTable.putValues(bean);
        contentResolver.update(uri, contentValues, AnalysisTable.POSID + "=?", new String[]{posid});
    }

    public static void deleteAnalysis(Context context, String posid) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = AnalysisTable.getContentUri();
        contentResolver.delete(uri, AnalysisTable.POSID + "=?", new String[]{posid});
    }

    public static int deleteAllAnalysis(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = AnalysisTable.getContentUri();
        return contentResolver.delete(uri, null, null);
    }

    public static AnalysisBean getOneAnalysis(Context context, String postId) {
        AnalysisBean bean = null;
        Uri uri = AnalysisTable.getContentUri();
        Cursor cursor = context.getContentResolver().query(uri, null,
                AnalysisTable.POSID + "=?", new String[]{postId}, null);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                bean = AnalysisTable.getValues(cursor);
            }
            cursor.close();
        }
        return bean;
    }

    public static List<AnalysisBean> getAllAnalysis(Context context) {
        ArrayList<AnalysisBean> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = AnalysisTable.getContentUri();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AnalysisBean bean = AnalysisTable.getValues(cursor);
                list.add(bean);
                //要往下一个跑
//                cursor.moveToNext();
            }
            cursor.close();
        }

        return list;
    }

    public static void addPushEvent(Context context, PushEvent bean) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = PushEventTable.getContentUri();
        ContentValues contentValues = PushEventTable.putValues(bean);
        contentResolver.insert(uri, contentValues);
    }


    public static int deleteAllPushEvent(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = PushEventTable.getContentUri();
        return contentResolver.delete(uri, null, null);
    }

    public static int deletePushEventFromPushSuccess(Context context, int pushSuccess) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = PushEventTable.getContentUri();
        return contentResolver.delete(uri, PushEventTable.IS_PUSH_SUCCESS + "=?",
                new String[]{String.valueOf(pushSuccess)});
    }

    public static List<PushEvent> getAllPushEvent(Context context) {
        ArrayList<PushEvent> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = PushEventTable.getContentUri();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PushEvent bean = PushEventTable.getValues(cursor);
                list.add(bean);
                //要往下一个跑
//                cursor.moveToNext();
            }
            cursor.close();
        }

        return list;
    }

    /**
     * 获取根据pushSuccess来进行获取的库里面的字段
     *
     * @param context
     * @param pushSuccess
     * @return
     */
    public static List<PushEvent> getPushEventPushSuccessList(Context context, int pushSuccess) {
        ArrayList<PushEvent> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = PushEventTable.getContentUri();
        Cursor cursor = contentResolver.query(uri,
                null,
                PushEventTable.IS_PUSH_SUCCESS + "=?",
                new String[]{String.valueOf(pushSuccess)}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PushEvent bean = PushEventTable.getValues(cursor);
                list.add(bean);
                //要往下一个跑
//                cursor.moveToNext();
            }
            cursor.close();
        }

        return list;
    }

    public static void updatePushEvent(Context context, PushEvent bean) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = PushEventTable.getContentUri();
        ContentValues contentValues = PushEventTable.putValues(bean);
        contentResolver.update(uri, contentValues, PushEventTable.TIMESTAMP + "=? AND " + PushEventTable.POSTID + "=?",
                new String[]{String.valueOf(bean.getTimestamp()), bean.getPostId()});
    }

    public static void updatePushEventList(Context context, List<PushEvent> beanList) {
        for (PushEvent pushEvent : beanList) {
            updatePushEvent(context, pushEvent);
        }
    }
}
