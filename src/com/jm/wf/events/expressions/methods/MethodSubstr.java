package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodSubstr extends MethodBase {

	@Override
	public DataValue evaluate(Object object)
	{
		DataValue value = null;
		DataValue param1 = null;
		DataValue param2 = null;
		DataValue param3 = null;
		
		boolean first  = true;
        boolean second = false;
        
        // Three parameters - do subString(string, beginIndex, endIndex)
        if (getParameters().size() == 3)
        {
	        for (ExpressionBase expression : getParameters())
	        {
	            if (first)
	            {
	                param1 = expression.evaluate(object);
	                
	                first  = false;
	                second = true;
	            }
	            else if (second)
	            {
	                param2 = expression.evaluate(object);
	                
	                second = false;
	            }
	            else
	            {
	                param3 = expression.evaluate(object);
	                
	                break;
	            }
	        }
        
	        if (param1 != null && param2 != null && param3 != null)
	        {
	        	String fullstr = param1.toString();
	        	String substr = null;
	        	int beginIndex = (int)param2.toInteger();
	        	int endIndex = (int)param3.toInteger();
	        	
	        	if (beginIndex < 0 || beginIndex >= endIndex || endIndex > fullstr.length())
	        	{
	        		// bad
	        		value = DataValueFactory.createNullValue();
	        	}
	        	else
	        	{
	        		// good
	        		substr = fullstr.substring(beginIndex, endIndex);
	        		
	        		value = DataValueFactory.createDataValue(substr, DataValueType.eString);
	        	}
	        }
        }
        // Two parameters - do subString(string, beginIndex)
        else if (getParameters().size() == 2)
        {
        	for (ExpressionBase expression : getParameters())
	        {
	            if (first)
	            {
	                param1 = expression.evaluate(object);
	                
	                first  = false;
	            }
	            else
	            {
	                param2 = expression.evaluate(object);
	                
	                break;
	            }
	        }
        	
        	if (param1 != null && param2 != null)
        	{
        		String fullstr = param1.toString();
	        	String substr = null;
	        	int beginIndex = (int)param2.toInteger();
	        	
	        	if (beginIndex < 0 || beginIndex > fullstr.length())
	        	{
	        		// bad
	        		value = DataValueFactory.createNullValue();
	        	}
	        	else
	        	{
	        		// good
	        		substr = fullstr.substring(beginIndex);
	        		
	        		value = DataValueFactory.createDataValue(substr, DataValueType.eString);
	        	}
        	}
        }
		
		return (value == null) ? DataValueFactory.createNullValue() : value;
	}

}
