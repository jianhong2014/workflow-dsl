package com.jm.wf.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <h1>CByteSize</h1>
 * 
 * <p>This is a collection of utility functions for manipulating strings that
 * contain byte (or bit) sizes.</p>
 */
public final class ByteSize
{
    private static Map<String, Double> conversions = new HashMap<String, Double>();
    
    private static Pattern sizes = Pattern.compile("(\\d+|\\d+\\.\\d+)\\s*(k|m|g|K|M|G)(b|B)?");
    
    private static boolean initialised = false;
    
    private static final int BYTESIZE = 1024;
    
    private static void initialiseConversions()
    {
        conversions.put("k", (double)BYTESIZE);                         // Kilobytes
        conversions.put("m", (double)(BYTESIZE * BYTESIZE));            // Megabytes
        conversions.put("g", (double)(BYTESIZE * BYTESIZE * BYTESIZE)); // Gigabytes
        conversions.put("K", (double)BYTESIZE);                         // Kilobytes
        conversions.put("M", (double)(BYTESIZE * BYTESIZE));            // Megabytes
        conversions.put("G", (double)(BYTESIZE * BYTESIZE * BYTESIZE)); // Gigabytes
    }
    
    synchronized private static void initialisePrivateVariables()
    {
        initialiseConversions();
        
        initialised = true;
    }
    
    synchronized private static boolean isInitialised()
    {
        return initialised;
    }
        
    /**
     * <h2>double convertToBytes(String size)</h2>
     *
     * <p>The size is expressed as a number of bytes followed by an
     * optional units string which can be either 1 or 2 characters.  
     * 
     * The first character is case insensitive and can be either of:
     * </p> 
     * <p>
     * <table border="1">
     * <thead>
     * <tr><th><b>1st Unit</b></th><th><b>Name</b></th><th><b>Conversion Factor</b></th><tr/>
     * </thead>
     * <tbody>
     * <tr><td>k</td><td>kilobytes</td><td>1024</td><tr/>
     * <tr><td>m</td><td>megabytes</td><td>1024 x k</td><tr/>
     * <tr><td>g</td><td>gigabytes</td><td>1024 x m</td><tr/>
     * </tbody>
     * </table>
     * 
     * <p>
     * The optional second character may be used to differentiate between bytes and bits and
     * can be either of:
     * <p>
     * 
     * <table border="1">
     * <thead>
     * <tr><th><b>2nd Unit</b></th><th><b>Name</b></th><th><b>Conversion Factor</b></th><tr/>
     * </thead>
     * <tbody>
     * <tr><td>B</td><td>bytes</td><td>x 1</td><tr/>
     * <tr><td>b</td><td>bits</td><td>bytes / 8</td><tr/>
     * </tbody>
     * </table>
     * </p>
     * <p>If the size string, or the second character, is left out then bytes are assumed.  
     * The numeric part of the size is multiplied by the appropriate conversion factor and the result
     * is returned as a decimal number of bytes.</p>
     * <p>
     * Example are 1024, 1K, 1KB which are all 1024 bytes. Other examples are 2MB, 4G, 4Mb etc.
     * </p>
     */
    public static double convertToBytes(String size) 
    {
        String component = null;
        double sizeInBytes = 0.0;
        double value  = 0.0;
                
        if (!isInitialised())
        {
            initialisePrivateVariables();
        }
        
        Matcher m = sizes.matcher(size.trim());
        
        if (m.matches())
        {
            for (int group = 0 ; group < m.groupCount() ; group ++)
            {
                component = m.group(group + 1);
                
                if ((component != null) && (component.length() != 0))
                {                
                    if (group == 0)
                    {
                        try
                        {
                            value = Double.parseDouble(component);
                        }
                        catch (NumberFormatException e)
                        {
                            value = 0.0;
                        }      
                    }
                    else if (group == 1) 
                    {
                        sizeInBytes = value * conversions.get(component);
                    }
                    else if (group == 2)
                    {
                        // Convert to bits
                        if (component.equals("b"))
                        {
                            sizeInBytes /= 8; 
                        }
                    }
                }
            }            
        }
        else
        {
            sizeInBytes = Double.parseDouble(size);
        }
        
        return (sizeInBytes);
    }    
    
    public static void main(String[] args)
    {
        boolean ok = false;
        System.out.println("-----------convertToBytes----------");
        
        ok = checkValue("1k", 1024.0);
        
        if (ok) { ok = checkValue("4194304", 4194304.0); }
        if (ok) { ok = checkValue("200KB",   204800.0); }
        if (ok) { ok = checkValue("5M",      5242880.0); }
        if (ok) { ok = checkValue("6 g",     6442450944.0); }
        if (ok) { ok = checkValue("4MB",     4194304.0); }
        if (ok) { ok = checkValue("3Mb",     393216.0); }
        if (ok) { ok = checkValue("4kb",     512.0); }
        if (ok) { ok = checkValue("4kB",     4096.0); }
        if (ok) { ok = checkValue("234 k",   239616.0); }
        if (ok) { ok = checkValue("20MB",    20971520.0); }     
        if (ok) { ok = checkValue("2 mb",    262144.0); }
        if (ok) { ok = checkValue("100Mb",   13107200.0); }
        if (ok) { ok = checkValue("4.5K",    4608.0); }
        if (ok) { ok = checkValue("12.2M",   12792627.2); }
        
        if (!ok)
        {
            System.out.println("TESTS FAILED");
        }
        else
        {
            System.out.println("TESTS PASSED");
        }
            
        System.out.println("-----------------------------------");
    }
    
    private static boolean checkValue (String size, double answer)
    {
        boolean success = false;
        double result = ByteSize.convertToBytes(size);
        
        if (result == answer)
        {
            System.out.println("String " + size + " = " + String.format("%.5f", result) + " bytes = OK");
            success = true;
        }
        else
        {
            System.out.println("Result " + String.format("%.5f", result) + " does not match answer " + answer + " for string " + size);
        }        
        
        return (success);
    }

}
