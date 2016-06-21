package com.jwson.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason_000 on 21/06/2016.
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils{
    public static final TimeZone LOCAL_TIMEZONE = TimeZone.getTimeZone("Hongkong");
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final DateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static Date strToDate(DateFormat dateFormat, String dateStr) throws ParseException {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        return null != dateFormat ? dateFormat.parse(dateStr) : strToDate(dateStr);
    }

    public static Date strToDate(String dateStr) throws ParseException {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        return dateFormat.parse(dateStr);
    }

    public static String dateToStr(Date date) {
        if (date != null) {
            return dateFormat.format(date);
        }
        return null;
    }

    /**
     * <p>
     * Checks if a date is today.
     * </p>
     *
     * @param date
     *            the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException
     *             if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        return isSameDay(date, Calendar.getInstance(LOCAL_TIMEZONE, DEFAULT_LOCALE).getTime());
    }

    /**
     * <p>
     * Checks if a calendar date is today.
     * </p>
     *
     * @param cal
     *            the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException
     *             if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        if (cal == null) {
            return false;
        }
        return isSameDay(cal, Calendar.getInstance(LOCAL_TIMEZONE, DEFAULT_LOCALE));
    }

    /**
     * <p>
     * Checks if the first date is before the second date ignoring time.
     * </p>
     *
     * @param date1
     *            the first date, not altered, not null
     * @param date2
     *            the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException
     *             if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }

    /**
     * <p>
     * Checks if the first calendar date is before the second calendar date ignoring time.
     * </p>
     *
     * @param cal1
     *            the first calendar, not altered, not null.
     * @param cal2
     *            the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException
     *             if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA))
            return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA))
            return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR))
            return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR))
            return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * <p>
     * Checks if the first date is after the second date ignoring time.
     * </p>
     *
     * @param date1
     *            the first date, not altered, not null
     * @param date2
     *            the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException
     *             if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    /**
     * <p>
     * Checks if the first calendar date is after the second calendar date ignoring time.
     * </p>
     *
     * @param cal1
     *            the first calendar, not altered, not null.
     * @param cal2
     *            the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException
     *             if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA))
            return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA))
            return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR))
            return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR))
            return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * <p>
     * Checks if a date is after today and within a number of days in the future.
     * </p>
     *
     * @param date
     *            the date to check, not altered, not null.
     * @param days
     *            the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException
     *             if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }

    /**
     * <p>
     * Checks if a calendar date is after today and within a number of days in the future.
     * </p>
     *
     * @param cal
     *            the calendar, not altered, not null
     * @param days
     *            the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException
     *             if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && !isAfterDay(cal, future));
    }

    /** Returns the given date with the time set to the start of the day. */
    public static Date getStart(Date date) {
        return clearTime(date);
    }

    /** Returns the given date with the time values cleared. */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance(LOCAL_TIMEZONE, DEFAULT_LOCALE);
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Determines whether or not a date has any time values (hour, minute, seconds or millisecondsReturns the given date with the time
     * values cleared.
     */

    /**
     * Determines whether or not a date has any time values.
     *
     * @param date
     *            The date.
     * @return true iff the date is not null and any of the date's hour, minute, seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /** Returns the given date with time set to the end of the day */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance(LOCAL_TIMEZONE, DEFAULT_LOCALE);
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less than any non-null date.
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null)
            return null;
        if (d1 == null)
            return d2;
        if (d2 == null)
            return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being greater than any non-null date.
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null)
            return null;
        if (d1 == null)
            return d2;
        if (d2 == null)
            return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /** The maximum date possible. */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

    /**
     * Return the first date of the month
     *
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * Return the last date of the month
     *
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * Return the first date of the week (Monday)
     *
     * @return
     */
    public static Date getFirstDateOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * Return the last date of the week
     *
     * @return
     */
    public static Date getLastDateOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * France: Monday, US: Sunday
     *
     * @return
     */
    public static Date getFirstDateOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }
        return calendar.getTime();
    }

    /**
     * DayDiff, compute the day difference between two dates.
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dayDiff(Date date1, Date date2) {
        return Math.abs((int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)));
    }

    /**
     * HourDiff, compute the hour difference between two dates.
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int hourDiff(Date date1, Date date2) {
        return Math.abs((int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60)));
    }

    /**
     * Parse date by pattern, <BR>
     * such as(10秒前, 10分钟前, 10小时前, 10天前, 1周前, 1年前); <BR>
     * or (10 seconds ago, 10 minutes ago, 10 hours ago, 10 days ago, 1 weeks ago, 1 years ago).
     *
     * @param dateStr
     * @param dateFormats
     * @return
     */
    public static Date parsePatternDate(String dateStr, String[] dateFormats) {
        Calendar cal = Calendar.getInstance();

        if (StringUtils.isEmpty(dateStr))
            return cal.getTime();

        if (null != dateFormats && dateFormats.length > 0) {
            try {
                return DateUtils.parseDate(dateStr, dateFormats);
            } catch (Exception e) {
                for (String dateFormat : dateFormats) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
                        return format.parse(dateStr);
                    } catch (Exception ex) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.CHINESE);
                            return format.parse(dateStr);
                        } catch (Exception exc) {

                        }

                    }
                }

            }
        }

        String pattern = "[\\s\\S]*(\\d+)\\s*(seconds|秒)[\\s\\S]*";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(dateStr);
        if (m.matches()) {
            String secondAgo = m.group(1);
            if (!StringUtils.isEmpty(secondAgo)) {
                try {
                    int second = Integer.parseInt(secondAgo);
                    cal.add(Calendar.SECOND, -second);
                } catch (NumberFormatException e) {

                }
            }

        }

        pattern = "[\\s\\S]*(\\d+)\\s*(minutes|分)[\\s\\S]*";
        p = Pattern.compile(pattern);
        m = p.matcher(dateStr);
        if (m.matches()) {
            String minAgo = m.group(1);
            if (!StringUtils.isEmpty(minAgo)) {
                try {
                    int min = Integer.parseInt(minAgo);
                    cal.add(Calendar.MINUTE, -min);
                } catch (NumberFormatException e) {

                }
            }
        }

        pattern = "[\\s\\S]*(\\d+)\\s*(hours|小)[\\s\\S]*";
        p = Pattern.compile(pattern);
        m = p.matcher(dateStr);
        if (m.matches()) {
            String hourAgo = m.group(1);
            if (!StringUtils.isEmpty(hourAgo)) {
                try {
                    int hour = Integer.parseInt(hourAgo);
                    cal.add(Calendar.HOUR_OF_DAY, -hour);
                } catch (NumberFormatException e) {

                }
            }
        }

        pattern = "[\\s\\S]*(\\d+)\\s*(days|天)[\\s\\S]*";
        p = Pattern.compile(pattern);
        m = p.matcher(dateStr);
        if (m.matches()) {
            String dayAgo = m.group(1);
            if (!StringUtils.isEmpty(dayAgo)) {
                try {
                    int day = Integer.parseInt(dayAgo);
                    cal.add(Calendar.DATE, -day);
                } catch (NumberFormatException e) {

                }
            }
        }

        pattern = "[\\s\\S]*(\\d+)\\s*(weeks|周|週)[\\s\\S]*";
        p = Pattern.compile(pattern);
        m = p.matcher(dateStr);
        if (m.matches()) {
            String weekAgo = m.group(1);
            if (!StringUtils.isEmpty(weekAgo)) {
                try {
                    int week = Integer.parseInt(weekAgo);
                    cal.add(Calendar.DATE, -week * 7);
                } catch (NumberFormatException e) {

                }
            }

        }

        pattern = "[\\s\\S]*(\\d+)\\s*(years|年)[\\s\\S]*";
        p = Pattern.compile(pattern);
        m = p.matcher(dateStr);
        if (m.matches()) {
            String yearAgo = m.group(1);
            if (!StringUtils.isEmpty(yearAgo)) {
                try {
                    int year = Integer.parseInt(yearAgo);
                    cal.add(Calendar.YEAR, -year);
                } catch (NumberFormatException e) {

                }
            }
        }

        return cal.getTime();
    }

    public static Date getLastYear() {
        Calendar cal = Calendar.getInstance(LOCAL_TIMEZONE, Locale.ENGLISH);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance(LOCAL_TIMEZONE, Locale.ENGLISH);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date getNDaysBefore(int nDay) {
        Calendar cal = Calendar.getInstance(LOCAL_TIMEZONE, DEFAULT_LOCALE);
        cal.add(Calendar.DAY_OF_YEAR, -1 * nDay);
        return cal.getTime();
    }
}
