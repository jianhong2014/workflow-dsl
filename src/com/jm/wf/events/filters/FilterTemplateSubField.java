package com.jm.wf.events.filters;

import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;


public interface FilterTemplateSubField
{
    public void setSubFieldName(String name);
    
    public String getSubFieldName();
    
    public List<FilterInterval> getSubFieldIntervals();
    
    public void addSubFieldInterval(String strExpr, DataValueType type)
    throws InvalidFilterIntervalException;
    
    public void addSubFieldInterval(FilterInterval expr);
    
    public void updateFieldType(DataValueType fieldType);
    
    public DataValue findMatch(DataValue value);
    
}
