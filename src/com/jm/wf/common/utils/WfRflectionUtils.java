package com.jm.wf.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WfRflectionUtils {
	static  String get = "get";
    static public Object getFieldValueByGetter(String fieldName, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodGetter = get+upperCaseFisrt(fieldName);
        Method getMethod = obj.getClass().getDeclaredMethod(methodGetter,null);
        return getMethod.invoke(obj);
    }
    static public String upperCaseFisrt(String name){
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
    
    static public Object[] getFieldAndType(String fieldName, Object obj) throws 
    SecurityException, IllegalArgumentException, IllegalAccessException {
    	Object[] ret = new Object[2];
    	Field field = null;
		try {
			field = obj.getClass().  
			        getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			ret[0] = null;
			ret[1] = Object.class;
			//e.printStackTrace();
			return ret;
		} 
    	field.setAccessible(true);
    	ret[0] = field.get(obj);
    	ret[1] = field.get(obj).getClass();
        
    	 return ret;
    }
 
}
