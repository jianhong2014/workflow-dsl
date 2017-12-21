package com.jm.wf.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Useful methods for formatting message strings for logging or exceptions.
 */
public class JmStringMessageUtils
{

    /*
     * The maximum number of Collection and Array elements used for messages
     */
    public static final int MAX_ELEMENTS = 50;
    public static final int DEFAULT_MESSAGE_WIDTH = 80;

    public JmStringMessageUtils()
    {

    }

    // public static String getFormattedMessage(String msg, Object[] arguments)
    // {
    // if (arguments != null)
    // {
    // for (int i = 0; i < arguments.length; i++)
    // {
    // arguments[i] = toString(arguments[i]);
    // }
    // }
    // return MessageFormat.format(msg, arguments);
    // }

    public static String getBoilerPlate(String message)
    {
        return getBoilerPlate(message, '*', DEFAULT_MESSAGE_WIDTH);
    }

    public static String getBoilerPlate(String message, char c, int maxlength)
    {
        return getBoilerPlate(new ArrayList<String>(Arrays.asList(new String[]
        { message })), c, maxlength);
    }

    public static String getBoilerPlate(List<String> messages, char c, int maxlength)
    {
        int size=0;
        StringBuffer buf = new StringBuffer(messages.size() * maxlength);
        int trimLength = maxlength - (c == ' ' ? 2 : 4);

        for (int i = 0; i < messages.size(); i++)
        {
        	
        	String message = messages.get(i);
        	if(message!=null)
        	{
            size = message.toString().length();
        	}
            if (size > trimLength)
            {
                String temp = messages.get(i).toString();
                int k = i;
                int x;
                int len;
                messages.remove(i);
                while (temp.length() > 0)
                {
                    len = (trimLength <= temp.length() ? trimLength : temp.length());
                    String msg = temp.substring(0, len);
                    x = msg.indexOf(JmSystemUtils.LINE_SEPARATOR);

                    if (x > -1)
                    {
                        msg = msg.substring(0, x);
                        len = x + 1;
                    }
                    else
                    {
                        x = msg.lastIndexOf(' ');
                        if (x > -1 && len == trimLength)
                        {
                            msg = msg.substring(0, x);
                            len = x + 1;
                        }
                    }
                    if (msg.startsWith(" "))
                    {
                        msg = msg.substring(1);
                    }

                    temp = temp.substring(len);
                    messages.add(k, msg);
                    k++;
                }
            }
        }

        buf.append(JmSystemUtils.LINE_SEPARATOR);
        if (c != ' ')
        {
            buf.append(JmStringUtils.repeat(c, maxlength));
        }

        for (int i = 0; i < messages.size(); i++)
        {
        	
        	String message =messages.get(i);
        	if(message!=null)
        	{
            buf.append(JmSystemUtils.LINE_SEPARATOR);
            if (c != ' ')
            {
                buf.append(c);
            }
            buf.append(" ");
            buf.append(messages.get(i));

            String osEncoding = JmCharSetUtils.defaultCharsetName();
            int padding;
            try
            {
                padding = trimLength - message.toString().getBytes(osEncoding).length;
                if (padding > 0)
                {
                    buf.append(JmStringUtils.repeat(' ', padding));
                }
            }
            catch (UnsupportedEncodingException ueex)
            {
                // ("Problem occured in encoding ");
            }
            // if (padding > 0)
            // {
            // buf.append(StringUtils.repeat(' ', padding));
            // }
            buf.append(' ');
            if (c != ' ')
            {
                buf.append(c);
            }
        	}
        }
        buf.append(JmSystemUtils.LINE_SEPARATOR);
        if (c != ' ')
        {
            buf.append(JmStringUtils.repeat(c, maxlength));
        }
        return buf.toString();
    }

    public static String truncate(String message, int length, boolean includeCount)
    {
        if (message == null)
        {
            return null;
        }
        if (message.length() <= length)
        {
            return message;
        }
        String result = message.substring(0, length) + "...";
        if (includeCount)
        {
            result += "[" + length + " of " + message.length() + "]";
        }
        return result;
    }

    public static String getString(byte[] bytes, String encoding)
    {
        try
        {
            return new String(bytes, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            // We can ignore this as the encoding is validated on start up
            return null;
        }
    }

}
