package com.mobi.core.db.use;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mobi.core.analysis.AnalysisBean;
import com.mobi.core.analysis.event.PushEvent;
import com.mobi.core.db.AdProvider;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/9 15:13
 * @Dec 操作数据库的类
 */
public class PushEventTable {
    //数据库中表名
    public static final String TABLE_NAME = "push_event";

    //表格的基本信息的字符串
    public static final String ID = "_id";


    public static final String EVENT = "event";
    public static final String POSTID = "postid";
    public static final String STYLE_TYPE = "style_type";
    public static final String SORT_TYPE = "sort_type";
    public static final String NETWORK = "network";
    public static final String MD5 = "md5";

    public static final String TYPE = "type";
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DEBUG = "debug";

    public static final String DAY = "day";
    public static final String TIME = "time";
    public static final String TIMESTAMP = "timestamp";
    public static final String BUNDLE = "bundle";
    public static final String APPID = "appId";


    public static final String CREATE_PUSH_EVENT_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT + " INTEGER, "
            + POSTID + " TEXT, "
            + STYLE_TYPE + " INTEGER, "
            + SORT_TYPE + " INTEGER, "
            + NETWORK + " TEXT, "
            + MD5 + " TEXT, "
            + TYPE + " INTEGER, "
            + CODE + " INTEGER, "
            + MESSAGE + " TEXT, "
            + DEBUG + " TEXT, "
            + BUNDLE + " TEXT, "
            + APPID + " TEXT, "
            + DAY + " TEXT, "
            + TIME + " TEXT, "
            + TIMESTAMP + " BIGINT)";


    //需要进行操作的uri对象
    private static Uri CONTENT_URI = null;

    //返回PeopleInfo表格操作的uri地址对象
    public static Uri getContentUri() {
        if (CONTENT_URI == null) {
            CONTENT_URI = Uri.withAppendedPath(AdProvider.AUTHORITY_URI, TABLE_NAME);
        }
        return CONTENT_URI;
    }

    public static ContentValues putValues(PushEvent info) {
        ContentValues values = new ContentValues();

        values.put(EVENT, info.getEvent());
        values.put(POSTID, info.getPostId());
        values.put(STYLE_TYPE, info.getStyleType());
        values.put(SORT_TYPE, info.getSortType());
        values.put(NETWORK, info.getNetwork());
        values.put(MD5, info.getMd5());

        values.put(TYPE, info.getType());
        values.put(CODE, info.getCode());
        values.put(MESSAGE, info.getMessage());
        values.put(DEBUG, info.getDebug());

        values.put(DAY, info.getDay());
        values.put(TIME, info.getTime());
        values.put(TIMESTAMP, info.getTimestamp());
        values.put(BUNDLE, info.getBundle());
        values.put(APPID, info.getAppId());

        return values;
    }

    public static PushEvent getValues(Cursor cursor) {

        int id = getIntValue(cursor, ID);
        int event = getIntValue(cursor, EVENT);
        String postid = getStringValue(cursor, POSTID);
        int styleType = getIntValue(cursor, STYLE_TYPE);
        int sortType = getIntValue(cursor, SORT_TYPE);
        String network = getStringValue(cursor, NETWORK);
        String md5 = getStringValue(cursor, MD5);
        int type = getIntValue(cursor, TYPE);
        int code = getIntValue(cursor, CODE);
        String message = getStringValue(cursor, MESSAGE);
        String debug = getStringValue(cursor, DEBUG);
        String bundle = getStringValue(cursor, BUNDLE);
        String appid = getStringValue(cursor, APPID);
        long timestamp = getLongValue(cursor, TIMESTAMP);
        String day = getStringValue(cursor, DAY);
        String time = getStringValue(cursor, TIME);

        PushEvent pushEvent = new PushEvent(event, styleType, postid, sortType, network, md5);
        pushEvent.setId(id);
        pushEvent.setType(type);
        pushEvent.setCode(code);
        pushEvent.setMessage(message);
        pushEvent.setDebug(debug);
        pushEvent.setBundle(bundle);
        pushEvent.setAppId(appid);
        pushEvent.setTimestamp(timestamp);
        pushEvent.setDay(day);
        pushEvent.setTime(time);

        return pushEvent;
    }


    /**
     * 获取每列的值
     *
     * @param cursor
     * @param name
     * @return
     */
    private static String getStringValue(Cursor cursor, String name) {
        String columnValue = "";
        if (cursor == null) {
            return columnValue;
        }
        //获取对应 数据库列名 的下标 如：name, address
        //获取不到返回值为 -1
        int columnName = cursor.getColumnIndex(name);
        if (columnName != -1) {
            columnValue = cursor.getString(columnName);
        }
        return columnValue;
    }
    /**
     * 获取每列的值
     *
     * @param cursor
     * @param name
     * @return
     */
    private static int getIntValue(Cursor cursor, String name) {
        int columnValue = 0;
        if (cursor == null) {
            return columnValue;
        }
        //获取对应 数据库列名 的下标 如：name, address
        //获取不到返回值为 <=0
        int columnName = cursor.getColumnIndex(name);
        if (columnName != -1) {
            columnValue = cursor.getInt(columnName);
        }
        return columnValue;
    }

    /**
     * 获取每列的值
     *
     * @param cursor
     * @param name
     * @return
     */
    private static long getLongValue(Cursor cursor, String name) {
        long columnValue = 0;
        if (cursor == null) {
            return columnValue;
        }
        //获取对应 数据库列名 的下标 如：name, address
        //获取不到返回值为 <=0
        int columnName = cursor.getColumnIndex(name);
        if (columnName != -1) {
            columnValue = cursor.getLong(columnName);
        }
        return columnValue;
    }
}
