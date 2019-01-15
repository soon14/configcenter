package com.asiainfo.configcenter.center.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {

    private static final String formatOne = "yyyy-MM-dd HH:mm:ss";
    private static final String formatCorn="ss mm HH dd MM ? yyyy";
    private static SimpleDateFormat formatter = new SimpleDateFormat(formatOne);
    private static SimpleDateFormat cronFormatter = new SimpleDateFormat(formatCorn);

    /**
     * 获取当前日期
     * @author bawy
     * @date 2018/7/19 23:21
     * @return 当前日期
     */
    public static Timestamp currentTime(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前日期指定秒数之后的日期
     * @author bawy
     * @date 2018/7/19 23:23
     * @param seconds 秒数
     * @return 指定日期
     */
    public static Timestamp afterTime(int seconds){
        return new Timestamp(System.currentTimeMillis() + seconds * 1000);
    }

    public static String timeFormat(long time){
        return formatter.format(new Date(time));
    }

    public static String formatCron(long time){
        java.util.Date date = new java.util.Date(time);
        return cronFormatter.format(date);
    }

}
