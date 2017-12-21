package com.jm.wf.source.service.filters;

import java.lang.reflect.InvocationTargetException;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.common.utils.WfRflectionUtils;

/**
 * ApiBodyParam represents request body
 * @author hongjian3
 *
 */
public class ApiBodyParam extends ApiParam {
	
	private  Object requestBody = null;
	
	public ApiBodyParam(Object bodyModel) {
		requestBody = bodyModel;
	}
	
	public DataValue getFieldValue(String fieldName) {
		Object fieldVlaue = null;
		try {
			fieldVlaue = (String) WfRflectionUtils.getFieldValueByGetter(fieldName, requestBody);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  DataValueFactory.createDataValue(fieldVlaue, DataValueType.eString);
	}
	public static  DataValue getFieldValue(String fieldName, Object request) {
		Object[] fieldInfo = null;
		try {
			fieldInfo = WfRflectionUtils.getFieldAndType(fieldName, request);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Object fieldVlaue = fieldInfo[0];
		if(fieldVlaue == null) {
			return DataValueFactory.createNullValue();
		}
		Class fieldtype = (Class) fieldInfo[1];
		String fieldTypeName = fieldtype.getSimpleName().toLowerCase();
		return  DataValueFactory.createDataValue(fieldVlaue, DataValueType.getDataValueType(fieldTypeName));	
	}
}
