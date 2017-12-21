package com.jm.wf.source.service.filters;


import com.jm.wf.common.datavalues.DataValue;

abstract public class ApiParam {
	abstract public DataValue getFieldValue(String fieldName);
}
