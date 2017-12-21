package com.jm.wf.common.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * <h1>CTimeStrings</h1>
 * 
 * <p>This is a collection of utility functions for manipulating strings that
 * contain time durations or ISO formatted dates.</p>
 */
public final class TimeStrings
{
    private static Map<String, Double> conversions = new HashMap<String, Double>();
    
    private static Pattern interval = Pattern.compile("(\\d+|\\d+\\.\\d+)\\s*(ms|s|m|h|d|w)?");
    
    private static Calendar calendar = null;
    
    private static boolean initialised = false;
    
    
    private static void initialiseConversions()
    {
        conversions.put("ms", 1.0);                        // Milliseconds
        conversions.put("s",  1000.0);                     // Seconds
        conversions.put("m",  1000.0 * 60);                // Minutes
        conversions.put("h",  1000.0 * 60 * 60);           // Hours
        conversions.put("d",  1000.0 * 60 * 60 * 24);      // Days
        conversions.put("w",  1000.0 * 60 * 60 * 24 * 7);  // Weeks
    }
    
    private static void initialiseCalendar()
    {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    }
    
    synchronized private static void initialisePrivateVariables()
    {
        initialiseConversions();
        
        initialiseCalendar();
        
        initialised = true;
    }
    
    synchronized private static boolean isInitialised()
    {
        return initialised;
    }
    
    
    /**
     * <h2>double convertToMillis(String duration)</h2>
     *
     * <p>The duration is expressed as a number (integer or decimal) followed by an
     * optional units string.  The units string is case insensitive and may be
     * one of the following:</p>
     * <p>
     * <table border="1">
     * <thead>
     * <tr><th><b>Units</b></th><th><b>Name</b></th><th><b>Conversion Factor</b></th><tr/>
     * </thead>
     * <tbody>
     * <tr><td>ms</td><td>milliseconds</td><td>1</td><tr/>
     * <tr><td>s</td><td>seconds</td><td>1000 x ms</td><tr/>
     * <tr><td>m</td><td>minutes</td><td>60 x s</td><tr/>
     * <tr><td>h</td><td>hours</td><td>60 x m</td><tr/>
     * <tr><td>d</td><td>days</td><td>24 x h</td><tr/>
     * <tr><td>w</td><td>weeks</td><td>7 x d</td><tr/>
     * </tbody>
     * </table>
     * </p>
     * <p>If the unit string is left out then seconds are assumed.  The numeric part of
     * the duration is multiplied by the appropriate conversion factor and the result
     * is returned as a decimal number of milliseconds.</p>
     */
    public static double convertToMillis(String duration)
    {
        if (!isInitialised())
        {
            initialisePrivateVariables();
        }
        
        double intervalMillis = 0.0;
        
        // Split the interval string into a number and a conversion string.
        // If the conversion string is missing then we will assume that it is
        // "s" (seconds).
        
        Matcher m = interval.matcher(duration.trim().toLowerCase());
        
        if (m.matches())
        {
            double value  = 0;
            double factor = 0;
            
            String grp1 = m.group(1);
            String grp2 = m.group(2);
            
            if (grp1 != null && grp1.length() != 0)
            {
                try
                {
                value = Double.parseDouble(m.group(1));
                }
                catch (NumberFormatException e)
                {
                    value = 0.0;
                }
            }
            else
            {
                value = 0.0;
            }
            
            if (grp2 != null && grp2.length() != 0)
            {
                factor = conversions.get(m.group(2));
            }
            else
            {
                factor = conversions.get("s");
            }
                
            intervalMillis = value * factor;
        }
        
        return intervalMillis;
    }
    
    /**
     * <h2>double convertToSeconds(String duration)</h2>
     *
     * <p>Calls convertToMillis() and divides the result by 1000.  Duration
     * format is as described in <b>convertToMillis()</b>.</p>
     */
    public static double convertToSeconds(String duration)
    {
        return convertToMillis(duration) / 1000;
    }
    
    /**
     * <h2>long epochMillisFromString(String theDate)</h2>
     * 
     * <p>Takes a date that is assumed to be in ISO format (YYYYMMDDhhmmss) and
     * converts it to milliseconds since the epoch.  If the date string is
     * shorter than full format it will be padded as necessary using:</p>
     * 
     * <table>
     * <tr><td> - </td><td>Month</td><td>01</td></tr>
     * <tr><td> - </td><td>Day</td><td>01</td></tr>
     * <tr><td> - </td><td>Hours</td><td>00</td></tr>
     * <tr><td> - </td><td>Minutes</td><td>00</td></tr>
     * <tr><td> - </td><td>Seconds</td><td>00</td></tr>
     * </table>
     *  
     * <p>At least the 4 digit year must be present.  The date string will have
     * all non-digits removed before being converted.  Time zone is GMT.</p>
     */
    public static long epochMillisFromString(String theDate) throws Exception
    {
        if (!isInitialised())
        {
            initialisePrivateVariables();
        }
        
        StringBuilder date = new StringBuilder().append(theDate.replaceAll("\\D", ""));
        
        while (date.length() < 8)
        {
            date.append("01");
        }
        
        while (date.length() < 14)
        {
            date.append("00");
        }
        
        String fixedUpDate = date.toString();
        
        calendar.clear();
        
        calendar.set(Integer.parseInt(fixedUpDate.substring( 0,  4)),
                     Integer.parseInt(fixedUpDate.substring( 4,  6)) - 1,
                     Integer.parseInt(fixedUpDate.substring( 6,  8)),
                     Integer.parseInt(fixedUpDate.substring( 8, 10)),
                     Integer.parseInt(fixedUpDate.substring(10, 12)),
                     Integer.parseInt(fixedUpDate.substring(12, 14)));
        
        return calendar.getTimeInMillis();
    }
    
    /**
     * <h2>long epochSecondsFromString(String theDate)</h2>
     *
     * <p>Calls <b>epochMillisFromString()</b> and divides the result by 1000.</p>
     */
    public static long epochSecondsFromString(String theDate) throws Exception
    {
        return epochMillisFromString(theDate) / 1000;
    }
    
    public static void main(String[] args) throws Exception
    {
        System.out.println("-----------convertToMillis----------");
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10ms")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10s")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10m")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10h")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10d")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10w")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10")));
        System.out.println("-----------convertToMillis----------");
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10MS")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10S")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10M")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10H")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10D")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10W")));
        System.out.println("-----------convertToMillis----------");
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10.9 ms")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("3.52 s")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10.3 m")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("10.25 h")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("13.2 d")));
        System.out.println(String.format("%.5f", TimeStrings.convertToMillis("11.9 w")));
        System.out.println("-----------convertToSeconds----------");
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10ms")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10s")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10m")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10h")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10d")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10w")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10")));
        System.out.println("-----------convertToSeconds----------");
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10MS")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10S")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10M")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10H")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10D")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10W")));
        System.out.println("-----------convertToSeconds----------");
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10.9 ms")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("3.52 s")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10.3 m")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("10.25 h")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("13.2 d")));
        System.out.println(String.format("%.5f", TimeStrings.convertToSeconds("11.9 w")));
        System.out.println("-----------epochMillisFromString----------");
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007-09-18 16:39:35")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007-09-18 16:39")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007-09-18 16")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007-09-18")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007-09")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007")));
        System.out.println("-----------epochMillisFromString----------");
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("20070918163935")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("200709181639")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007091816")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("20070918")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("200709")));
        System.out.println(String.format("%d", TimeStrings.epochMillisFromString("2007")));
        System.out.println("-----------epochSecondsFromString----------");
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007-09-18 16:39:35")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007-09-18 16:39")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007-09-18 16")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007-09-18")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007-09")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007")));
        System.out.println("-----------epochSecondsFromString----------");
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("20070918163935")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("200709181639")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007091816")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("20070918")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("200709")));
        System.out.println(String.format("%d", TimeStrings.epochSecondsFromString("2007")));
        System.out.println("---------------------");
    }
}
