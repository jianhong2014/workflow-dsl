package com.jm.wf.events.filters;

import java.util.List;


import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;

public interface FilterTemplate
{
    public FilterTemplateField createFilterTemplateField(String fieldText)
    throws InvalidFilterFormatException;
    
    public void addTemplateField(String templateField) throws InvalidFilterFormatException;
    
    public void addTemplateField(FilterTemplateField field);
    
    public List<FilterTemplateField> getFields();
    
    public DataValueType getDataValueType();

    public int getTruncationInterval();

    public void setTruncationInterval(int truncationInterval);
    
    public boolean matches(Object object);

    public DataValue getValue(Object object);
    
    public Filter createInstance(Object object);
    
    public Filter createInstance(Object object, boolean allowNulls);
    
}
