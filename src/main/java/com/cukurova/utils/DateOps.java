package com.cukurova.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateOps {

    public int periodOfDays(int dayCount) {
        DateOps date = new DateOps();
        return dayCount * 24 * 60 * 60 * 1000;
    }

    public static Date addDaysToDate(Date date, long days) {
        days = days * 24 * 60 * 60 * 1000;
        Long sum = date.getTime() + days;

        Date sumDate = new Date(sum);
        return sumDate;
    }

    public static Date addMinsToDate(Date date, long mins) {
        mins = mins * 60 * 1000;
        Long sum = date.getTime() + mins;

        Date sumDate = new Date(sum);
        return sumDate;
    }

    public static String getTodaysDateFormatted() throws ParseException {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(date);

    }

    public static String dateToStringFormatted(Date date) {
        String rslt = "";
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            rslt = df.format(date);
        } catch (Exception pe) {
            return "";
        }
        return rslt;
    }

    public static Date stringToDate(String date) throws ParseException {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.parse(date);

    }

    public static Date daysAfterToday(Long dayCount) {
        Date date = new Date();
        dayCount = dayCount * 24 * 60 * 60;
        Long sum = (date.getTime() / 1000) + dayCount;
        date = new Date(sum * 1000);

        return date;
    }

    public static int getDayIntervalFromToday(Date d2) {
        Date d1 = new Date();
        long diff = d2.getTime() - d1.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Date getDateFromTimeStamp(Timestamp timestamp) {
        Date date = new Date();
        if (timestamp != null) {
            date = new Date(timestamp.getTime());
        }
        return date;
    }

    public static Date getNthDayOfMonth(Date date, int n) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, n);

        return calendar.getTime();
    }

    public static LocalDate getMonthFirstDate(Date date) {
        return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).withDayOfMonth(1);
    }

    public static LocalDate getMonthLastDate(Date date) {
        return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).plusMonths(1).withDayOfMonth(1).minusDays(1);
    }

    public static Date toDate(String string) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        Date date = format.parse(string);
        return date;
    }
    public static Date toDateTime(String string) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
        Date date = format.parse(string);
        return date;
    }

    public static String toFormattedString(Date date) {

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
            return format.format(date);
        } catch (Exception e) {
            return null;
        }
    }

//    public static Date formatDate(Date date) throws ParseException{
//        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        return df.format(date);
//    }
}
