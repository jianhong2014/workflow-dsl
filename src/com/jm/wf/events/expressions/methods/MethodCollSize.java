package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodCollSize extends MethodBase
{
	@Override
    public DataValue evaluate(Object object)
    {
        DataValue value = null;
        
        for (ExpressionBase expression : getParameters())
        {
            value = expression.evaluate(object);
                        
            break;
        }

        if (value == null)
        {
            value = DataValueFactory.createNullValue();
        }
        
        /*
         * This method returns the size of a data value collection, or zero
         * if it's not a collection.
         */        
        if (value instanceof DataValueCollection)
        {
        	return DataValueFactory.createDataValue(value.size(),
        											DataValueType.eInteger);  
        }
        else
        {
        	return DataValueFactory.createDataValue(0, 
        											DataValueType.eInteger);  
        }      
    }
}

/*** END OF FILE ***/
