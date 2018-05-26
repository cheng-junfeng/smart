package com.base.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat DEFAULT_SDF_DAY = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat DEFAULT_SDF_HOUR = new SimpleDateFormat("HH:mm", Locale.getDefault());

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param milliseconds 毫秒时间戳
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds) {
        return milliseconds2String(milliseconds, DEFAULT_SDF);
    }

    public static String milliseconds2StringMsg(long milliseconds) {
        if(isToday(milliseconds)){
            return milliseconds2String(milliseconds, DEFAULT_SDF_HOUR);
        }else{
            return milliseconds2String(milliseconds, DEFAULT_SDF_DAY);
        }
    }

    public static String milliseconds2StringMsg(String milliseconds) {
        if(TextUtils.isEmpty(milliseconds)){
            return milliseconds;
        }
        long inTime = 0L;
        try{
            inTime = Long.parseLong(milliseconds);
        }catch (Exception e){
        }

        return milliseconds2StringMsg(inTime);
    }

    public static boolean isToday(long inTime){
        if(DEFAULT_SDF_DAY.format(new Date(inTime)).toString().equals(DEFAULT_SDF_DAY.format(new Date()).toString())){//格式化为相同格式
            return true;
        }else {
            return false;
        }
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为yyyy-MM-dd HH:mm</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Milliseconds(String time) {
        return string2Milliseconds(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        if (!TextUtils.isEmpty(time)) {
            try {
                return format.parse(time).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
