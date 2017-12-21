package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodSetMerge extends MethodBase
{
	/*
	 * This method expects two sets and returns a single set after merging them
	 * together. DataValues which are not a set are ignored therefore if both
	 * values are, for example, set to NO_DATA, then an empty set will be returned.
	 */
	public DataValue evaluate(Object object) 
	{
		DataValueCollection set =
            (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eSet);
        
        for (ExpressionBase expression : getParameters())
        {
            DataValue value = expression.evaluate(object);
                        
            if (value != null)
            {
            	if (value instanceof DataValueCollection)
            	{
            		set.addAll((DataValueCollection) value);
            	}
            }
        }

        return (DataValue) set;
	}
}


