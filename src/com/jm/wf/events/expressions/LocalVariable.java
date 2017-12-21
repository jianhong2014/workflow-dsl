package com.jm.wf.events.expressions;

import com.jm.wf.common.datavalues.DataValue;

public class LocalVariable
{
    private String               name    = null;
    private FieldValueExpression context = null;

    public LocalVariable(String               name,
                         FieldValueExpression context)
    {
        this.name    = name;
        this.context = context;
    }
    
    public void setValue(DataValue value)
    {
        if (name != null && context != null)
        {
            context.setLocalVariable(name, value);
        }
    }
    
    public DataValue getValue()
    {
        DataValue value = null;
        
        if (name != null && context != null)
        {
            value = context.getLocalVariable(name);
        }
        
        return value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public FieldValueExpression getContext()
    {
        return context;
    }

    public void setContext(FieldValueExpression context)
    {
        this.context = context;
    }
    
    public String toString()
    {
        return name;
    }
}
