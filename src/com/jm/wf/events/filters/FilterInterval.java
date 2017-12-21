package com.jm.wf.events.filters;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;


public interface FilterInterval
{
    public boolean matches(DataValue value);
    
    public void updateFieldType(DataValueType fieldType);
}
