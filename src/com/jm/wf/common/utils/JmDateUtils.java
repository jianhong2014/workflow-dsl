/*
 * $Id: CaafDateUtils.java 8077 2007-08-27 20:15:25Z aperepel $
 * --------------------------------------------------------------------------------------
 */

package com.jm.wf.common.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;





/**
 * <code>DateUtils</code> contains some useful methods dealing date/time
 * conversion, formatting etc.
 */
// @ThreadSafe
public class JmDateUtils extends org.apache.commons.lang.time.DateUtils
{

    public static String getTimeStamp(String format)
    {
        // Format the current time.
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    public static String formatTimeStamp(Date dateTime, String format)
    {
        // Format the current time.
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(dateTime);
    }
    
    public static String getFormattedDuration(long mills)
    {
        long days = mills / 86400000;
        mills = mills - (days * 86400000);
        long hours = mills / 3600000;
        mills = mills - (hours * 3600000);
        long mins = mills / 60000;
        mills = mills - (mins * 60000);
        long secs = mills / 1000;
        mills = mills - (secs * 1000);

        StringBuffer bf = new StringBuffer(60);
        bf.append(days).append(" ").append("days").append(", ");
        bf.append(hours).append(" ").append("hours").append(", ");
        bf.append(mins).append(" ").append("mins").append(", ");
        bf.append(secs).append(".").append(mills).append(" ").append("secs").append(", ");
        return bf.toString();
    }

    public static String getStringFromDate(Date date, String format)
    {
        // converts from date to strin using the standard TIME_STAMP_FORMAT
        // pattern
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date getDateFromString(String date, String format)
    {
        // The date must always be in the format of TIME_STAMP_FORMAT
        // i.e. JAN 29 2001 22:50:40 GMT
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);

        // Parse the string back into a Time Stamp.
        return formatter.parse(date, pos);
    }

}
