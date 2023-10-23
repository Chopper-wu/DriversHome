package com.blue.corelib.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chopper on 2021/4/25
 * desc : 时间日期转换类
 */
public class DateUtil {
    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToTime(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToTime2(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * 将时间戳转换为日期
     */
    public static String stampToDate(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * 将时间戳转换为日期
     */
    public static String stampToDateTwo(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @Desction : 得到当前的日期
     * @auther : Chopper
     * Creater at 2019/3/22 14:25
     */
    public static String getNowDateMs() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return f.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * @Desction : 得到当前的日期
     * @auther : Chopper
     * Creater at 2019/3/22 14:25
     */
    public static String getNowDate() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
            return f.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @Desction : 得到当前的日期
     * @auther : Chopper
     * Creater at 2019/3/22 14:25
     */
    public static String getNowDateTwo() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            return f.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static long timeStamp() {
        long time = System.currentTimeMillis();
        long t = time / 1000;
        return t;
    }
    /**
     * 比较两个日期Date2比Date1(年月日)多的天数,(只考虑天数不考虑时间)
     * 例如:2017-01-25 23:59:59与 2017-01-26 00:00:00   返回1
     * 2017-01-25 与 2017-01-25   返回0
     * 2017-01-28 与 2017-01-25   返回-3
     * @author terry.peng
     */
    public static int differDayQty(Date Date1,Date Date2){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.clear();
        calendar.setTime(Date1);
        int day1 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
        int year1 = calendar.get(java.util.Calendar.YEAR);
        calendar.setTime(Date2);
        int day2 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
        int year2 = calendar.get(java.util.Calendar.YEAR);
        if(year1 == year2){//同一年
            return day2-day1;
        }else if(year1<year2){//Date1<Date2
            int days = 0;
            for (int i = year1; i < year2; i++) {
                if(i%4 == 0 && i%100!=0 || i%400 == 0){//闰年
                    days += 366;
                }else {
                    days += 365;
                }
            }
            return days+(day2 - day1);
        }else{//Date1>Date2
            int days = 0;
            for (int i = year2; i < year1; i++) {
                if(i%4 == 0 && i%100!=0 || i%400 == 0){
                    days += 366;
                }else {
                    days += 365;
                }
            }
            return 0-days+(day2-day1);
        }
    }
    /**
     * @Desction : 传入时间戳和当前时间比较天数
     * @auther : Chopper
     * Creater at 2019/4/20 20:39
     */
    public static int daysOfTwo(String time) {
        int result = 0;
        try {
            String ftime = stampToDateTwo(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //跨年的情况会出现问题哦
            //如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
            Date fDate = sdf.parse(ftime);
            Date oDate = sdf.parse(getNowDateTwo());
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(fDate);
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(oDate);
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            result = day1 - day2;

            result = differDayQty(oDate,fDate);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * @Desction : 传入时间戳和当前时间比较天数
     * @auther : Chopper
     * Creater at 2019/4/20 20:39
     */
    public static int daysOfOne(String time) {
        int result = 0;
        try {
            String ftime = stampToDateTwo(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //跨年的情况会出现问题哦
            //如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
            Date fDate = sdf.parse(ftime);
            Date oDate = sdf.parse(getNowDateTwo());
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(fDate);
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(oDate);
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            result = day2 - day1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * @Desction : 传入时间戳和当前时间比较天数
     * @auther : Chopper
     * Creater at 2019/4/20 20:39
     */
    public static String timeOfOne(long fortime, long currentTime) {
        try {
            long nowday, nowHours, nowMinutes;
            nowday = (fortime - currentTime) / (3600 * 24);
            nowHours = (fortime - currentTime) / (3600);
            nowMinutes = (fortime - currentTime) / (60);
            if (nowday > 0) {
                return nowday + "天后";
            } else if (nowday == 0) {
                if (nowHours > 0) {
                    return nowHours + "小时后";
                } else if (nowHours == 0) {
                    if (nowMinutes > 0) {
                        return nowMinutes + "分钟后";
                    } else {
                        return "今天";
                    }
                } else {
                    return "今天";
                }
            } else {
                return "今天";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "今天";
        }
    }
    public static String transformationMonth(String value){
        if (TextUtils.isEmpty(value)){
            return "";
        }
        if (value.equals("01")||value.equals("1")){
            return "1月";
        }else if (value.equals("02")||value.equals("2")){
            return "2月";
        }else if (value.equals("03")||value.equals("3")){
            return "3月";
        }else if (value.equals("04")||value.equals("4")){
            return "4月";
        }else if (value.equals("05")||value.equals("5")){
            return "5月";
        }else if (value.equals("06")||value.equals("6")){
            return "6月";
        }else if (value.equals("07")||value.equals("7")){
            return "7月";
        }else if (value.equals("08")||value.equals("8")){
            return "8月";
        }else if (value.equals("09")||value.equals("9")){
            return "9月";
        }else if (value.equals("10")){
            return "10月";
        }else if (value.equals("11")){
            return "11月";
        }else if (value.equals("12")){
            return "12月";
        }else {
            return "";
        }
    }
}
