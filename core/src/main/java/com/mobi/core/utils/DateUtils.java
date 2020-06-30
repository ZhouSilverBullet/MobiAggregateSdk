package com.mobi.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author : liangning
 * date : 2019-11-18  13:50
 */
public class DateUtils {

    /**
     * 获取当前时间 截止到天
     *
     * @return
     */
    public static String getStringDateDay() {
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前时间 截止到分
     *
     * @return
     */
    public static String getStringDateMin() {
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前毫秒级时间戳
     *
     * @return
     */
    public static long getNowTimeLong() {
        return System.currentTimeMillis();
    }

}
