package com.baymin.springboot.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 转换时间格式，加减天数
     */
    public static Date getDateByDay(Date date, int oper) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + oper);
        return c.getTime();
    }

    /**
     * 转换时间格式，加减分钟
     */
    public static Date getDateByMin(Date date, int oper) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + oper);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 转换时间格式，加减天数
     */
    public static Date getDate(String dateFormat, int oper) throws Exception {
        Calendar c = Calendar.getInstance();
        Date dt = sdf.parse(dateFormat);
        c.setTime(dt);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + oper);
        return c.getTime();
    }

    /**
     * 转换时间格式
     */
    public static Date getDate(String dateFormat) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateFormat);
    }

    /**
     * 转换时间格式
     */
    public static Date getDate(String dateFormat,String pattern) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateFormat);
    }

    public static Date getFirstDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getLastDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        return isTheDay(date, new Date());
    }

    /**
     * 是否是指定日期
     *
     * @param date
     * @param day
     * @return
     */
    public static boolean isTheDay(final Date date, final Date day) {
        return date.getTime() >= dayBegin(day).getTime()
                && date.getTime() <= dayEnd(day).getTime();
    }

    /**
     * 获取指定时间的那天 00:00:00 的时间
     *
     * @param dateStr
     * @return
     */
    public static Date dayBegin(final String dateStr) {
        if(StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return dayBegin(sdf.parse(dateStr));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取指定时间的那天 23:59:59 的时间
     *
     * @param dateStr
     * @return
     */
    public static Date dayEnd(final String dateStr) {
        if(StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return dayEnd(sdf.parse(dateStr));
        } catch (ParseException e) {
            return null;
        }
    }

}