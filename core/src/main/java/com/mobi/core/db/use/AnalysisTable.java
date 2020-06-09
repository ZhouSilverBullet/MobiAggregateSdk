package com.mobi.core.db.use;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mobi.core.analysis.AnalysisBean;
import com.mobi.core.db.AdProvider;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/9 15:13
 * @Dec 操作数据库的类
 */
public class AnalysisTable {
    //数据库中表名
    public static final String TABLE_NAME = "analysis";

    //表格的基本信息的字符串
    public static final String ID = "_id";
    public static final String DAY = "day";
    public static final String TIME = "time";
    public static final String DEVICEID = "deviceid";
    public static final String PLATFORM = "platform";
    public static final String SDKV = "sdkv";
    public static final String CHANNEL_NO = "channel_no";
    public static final String NETWORK = "network";
    public static final String POSID = "posid";
    public static final String PV = "pv";
    public static final String CLICK = "click";


    public static final String CREATE_ANALYSIS_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DAY + " TEXT, "
            + TIME + " TEXT, "
            + DEVICEID + " TEXT, "
            + PLATFORM + " TEXT, "
            + SDKV + " TEXT, "
            + CHANNEL_NO + " TEXT, "
            + NETWORK + " TEXT, "
            + POSID + " TEXT, "
            + PV + " INTEGER, "
            + CLICK + " INTEGER)";


    //需要进行操作的uri对象
    private static final Uri CONTENT_URI = Uri.withAppendedPath(AdProvider.AUTHORITY_URI, TABLE_NAME);

    //返回PeopleInfo表格操作的uri地址对象
    public static Uri getContentUri() {
        return CONTENT_URI;
    }

    public static ContentValues putValues(AnalysisBean info) {
        ContentValues values = new ContentValues();
        values.put(DAY, info.getDay());
        values.put(TIME, info.getTime());
        values.put(DEVICEID, info.getDeviceid());
        values.put(PLATFORM, info.getPlatform());
        values.put(SDKV, info.getSdkv());
        values.put(CHANNEL_NO, info.getChannel_no());
        values.put(NETWORK, info.getNetwork());
        values.put(POSID, info.getPosid());
        values.put(PV, info.getPv());
        values.put(CLICK, info.getClick());
        return values;
    }

    public static AnalysisBean getValues(Cursor cursor) {
        int id = getIntValue(cursor, ID);
        String day = getStringValue(cursor, DAY);
        String time = getStringValue(cursor, TIME);
        String deviceid = getStringValue(cursor, DEVICEID);
        String platform = getStringValue(cursor, PLATFORM);
        String sdkv = getStringValue(cursor, SDKV);
        String channel_no = getStringValue(cursor, CHANNEL_NO);
        String network = getStringValue(cursor, NETWORK);
        String posid = getStringValue(cursor, POSID);
        int pv = getIntValue(cursor, PV);
        int click = getIntValue(cursor, CLICK);

        AnalysisBean analysisBean = new AnalysisBean(network, posid, pv, click);
        analysisBean.setId(id);
        analysisBean.setDay(day);
        analysisBean.setTime(time);
        analysisBean.setDeviceid(deviceid);
        analysisBean.setPlatform(platform);
        analysisBean.setSdkv(sdkv);
        analysisBean.setChannel_no(channel_no);

        return analysisBean;
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
        //获取不到返回值为 -1
        int columnName = cursor.getColumnIndex(name);
        if (columnName != -1) {
            columnValue = cursor.getInt(columnName);
        }
        return columnValue;
    }
}
