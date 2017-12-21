package com.jm.wf.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class JmCharSetUtils extends org.apache.commons.lang.CharSetUtils
{

    public static String defaultCharsetName()
    {
        try
        {
            if (JmSystemUtils.IS_JAVA_1_4)
            {
                return new OutputStreamWriter(new ByteArrayOutputStream()).getEncoding();
            }
            else
            {
                Class<?> target = Charset.class;
                Method defaultCharset = target.getMethod("defaultCharset", JmArrayUtils.EMPTY_CLASS_ARRAY);
                Charset cs = (Charset) defaultCharset.invoke((Object) target, (Object[]) (Class[]) null);
                return cs.name();
            }
        }
        catch (Exception ex)
        {
            throw new Error(ex);
        }
    }
}
