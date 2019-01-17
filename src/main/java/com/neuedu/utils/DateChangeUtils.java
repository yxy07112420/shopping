package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * date与String类型的转换
 */
public class DateChangeUtils {
    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";

    /**
     * 时间转为string类型
     * @param date
     * @return
     */
    public static String dateToString(Date date,String format){
       DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }
    public static String dateToString(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    /**
     * sting转date
     */

    public static Date stringToDate(String strTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strTime);
        return dateTime.toDate();
    }
    public static Date stringToDate(String strTime,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strTime);
        return dateTime.toDate();
    }

    public static void main(String[] args) {
        System.out.println(stringToDate("2019-01-09 12:29:51"));
    }

}
