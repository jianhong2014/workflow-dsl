package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodInteger extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue value = null;
        
        DataValue param1 = null;
        DataValue param2 = null;
        
        boolean first = true;
        for (ExpressionBase expression : getParameters())
        {
        	if (first)
        	{
        		param1 = expression.evaluate(object);
        		first = false;
        	}
        	else
        	{
        		param2 = expression.evaluate(object);
        		break;
        	}
        }
        
        // No value to convert
        if (param1 == null)
        {
        	value = DataValueFactory.createNullValue();
        }
        // Standard conversion - @integer(value)
        else if (param2 == null)
        {
        	value = DataValueFactory.createDataValue(param1,
                    								 DataValueType.eInteger);
        }
        // Specified Radix - @integer(value, radix)
        else
        {
        	long convertedInt = 0;
        	String valueStr = param1.toString();
        	int radix = (int)param2.toInteger();
        	
        	if (radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX)
        	{
        		try
        		{
        			convertedInt = Long.parseLong(valueStr, radix);
        		}
        		catch (NumberFormatException nfe)
        		{
        			convertedInt = 0;
        		}
        	}
        	
        	value = DataValueFactory.createDataValue(convertedInt,
                    								 DataValueType.eInteger);
        }
        
        return value;
    }

}
