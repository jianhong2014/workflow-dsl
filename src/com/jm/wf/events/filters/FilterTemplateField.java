package com.jm.wf.events.filters;


import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.common.datavalues.InvalidDataTypeException;


public interface FilterTemplateField
{
    public FilterTemplateSubField createFilterTemplateSubField(String strSubFieldName);
    
    public void addSubField(String subFieldText);
    
    public void instantiateField(boolean inst);
    
    public boolean isInstantiated();
    
    public void negateField(boolean neg);
    
    public boolean isNegated();
    
    public void setFormatString(String fmtStr);
    
    public String getFormatString();
    
    public DataValueType getFieldType();
    
    public void addSubField(FilterTemplateSubField subField);
    
    public DataValue getValue(Object object) throws InvalidDataTypeException, FilterFieldNotFoundException;
    
    public List<FilterTemplateSubField> getSubFields ();    
}
