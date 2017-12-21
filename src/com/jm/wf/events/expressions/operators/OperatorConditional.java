package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.events.expressions.ExpressionBase;

public class OperatorConditional extends OperatorBase
{
    private ExpressionBase condition = null;

    public OperatorConditional()
    {
        setOperatorName("?:");
    }
    
	@Override
	public DataValue evaluate(Object object)
	{
	    DataValue condValue = condition.evaluate(object);
	    
	    if (condValue == null || condValue instanceof DataValueNull)
	    {
	        return DataValueFactory.createNullValue();
	    }
	    
		if (condValue.toBoolean())
		{
			return getLhs().evaluate(object);
		}

		return getRhs().evaluate(object);
	}

    public ExpressionBase getCondition()
    {
        return condition;
    }

    public void setCondition(ExpressionBase condition)
    {
        this.condition = condition;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();

        str.append(condition).append(" ? ").append(getLhs()).append(" : ").append(getRhs());
        
        return str.toString();
    }
}
