package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodIntSplit extends MethodBase
{
	@Override
	public DataValue evaluate(Object object)
	{
        DataValue param1 = null;
        DataValue param2 = null;
        DataValue value  = null;
        
        boolean first  = true;
                
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

        DataValueCollection list =
            (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eList);
        
        if (param1 != null && param2 == null)
        {
        	String str = param1.toString();
        	int index = 0;
        	for (int i=0 ;i< str.length()/2 ; i++)
        	{
        		String hexStr = str.substring(index, index+2);
        		index+=2;
        		String valueStr = "0x" + hexStr;
        		list.add(DataValueFactory.createDataValue(valueStr,DataValueType.eInteger));
        	}      	
        }
        else
        {
            value = DataValueFactory.createNullValue();
        }
        
        return (value == null ? (DataValue) list : value);
    }

}
