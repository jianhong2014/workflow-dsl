package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodIsNull extends MethodBase
{
	/*
	 * This method returns true if a parameter is null, otherwise false.
	 * 
	 * It should be enhanced to support list, map, set etc to return true
	 * if all elements are empty.
	 */
	@Override
    public DataValue evaluate(Object object)
    {
        DataValue param1 = null;
        boolean   result = false;
        
        for (ExpressionBase expression : getParameters())
        {
        	param1 = expression.evaluate(object);
        	
        	break;
        }
        
        if (param1 == null || param1 instanceof DataValueNull)
        {
        	result = true;
        }
        
        return DataValueFactory.createDataValue(result ? 1 : 0,
                                                DataValueType.eBoolean);
    }
}
