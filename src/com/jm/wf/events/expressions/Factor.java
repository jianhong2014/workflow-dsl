package com.jm.wf.events.expressions;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueString;
import com.jm.wf.events.filters.FilterTemplate;

public class Factor extends ExpressionBase
{
    protected DataValue value    = null;
    protected LocalVariable  variable = null;
	protected FilterTemplate template = null;

	@Override
	public DataValue evaluate(Object object)
	{
	    if (variable != null)
	    {
	        return variable.getValue();
	    }
	    else if (template != null)
		{
			DataValue tmpVal =  template.getValue(object);
			
			if (tmpVal == null)
			{
			    try
			    {
			        tmpVal = new DataValueNull();
			    }
			    catch (Exception e) {}
			    
			    return tmpVal;
			}

			value = tmpVal;
		}
		
		return value;
	}

	public DataValue getValue()
	{
		return value;
	}

	public void setValue(DataValue value)
	{
		this.value = value;
	}

    public LocalVariable getVariable()
    {
        return variable;
    }

    public void setVariable(LocalVariable variable)
    {
        this.variable = variable;
    }

    public FilterTemplate getTemplate()
    {
        return template;
    }

    public void setTemplate(FilterTemplate template)
    {
        this.template = template;
    }
    
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        
        if (variable != null)
        {
            str.append(variable.toString());
        }
        else if (template != null)
        {
            str.append(template.toString());
        }
        else
        {
            if (value instanceof DataValueString)
            {
                if (value.toString().contains("'"))
                {
                    str.append("\"").append(value).append("\"");
                }
                else
                {
                    str.append("'").append(value).append("'");
                }
            }
            else
            {
                str.append(value);
            }
        }
            
        return str.toString();
    }

}
