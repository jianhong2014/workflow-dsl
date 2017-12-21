package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodFirstValue extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue value = null;
        
        for (ExpressionBase expression : getParameters())
        {
            value = expression.evaluate(object);
            
            if (value != null && !(value instanceof DataValueNull))
                break;
        }
        
        if (value == null)
        {
            value = DataValueFactory.createNullValue();
        }

        return value;
    }

}
