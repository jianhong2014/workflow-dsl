package com.jm.wf.events.filters;

import java.util.List;

import com.jm.wf.common.datavalues.DataValue;

public interface Filter
{
    public void addFieldValue(DataValue value);
    
    public List<DataValue> getFieldValues();
    
    public DataValue getValue();
    
    public String toString(String separator);

}
