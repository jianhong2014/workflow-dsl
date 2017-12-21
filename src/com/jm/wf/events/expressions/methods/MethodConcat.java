package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;

/**
 * This Method expects two parameters for evaluation:
 * 
 * 	1. A data value
 * 	2. A data value to append to the end of the 1st data value
 * 
 * The result is a single data value which contains the value of the 
 * two data value strings.
 */

public class MethodConcat extends MethodBase
{
	@Override
    public DataValue evaluate(Object object)
    {
		DataValue result = null;
        
        DataValue first = getParameters().get(0).evaluate(object);
        DataValue second  = getParameters().get(1).evaluate(object);
                
        if (first != null && second != null)
        {      
        	StringBuilder sb = new StringBuilder();
        	
        	sb.append(first.toString()).append("+").append(second.toString());
        	        	
        	result = DataValueFactory.createDataValue(sb.toString(), DataValueType.eString);
        }
        else if (first != null)
        {
        	result = first;
        }
        else if (second != null)
        {
        	result = second;
        }

        return result;
    }
}

/** END OF FILE ***/
