package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodReal extends MethodBase
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
        
        return DataValueFactory.createDataValue(value,
                                                DataValueType.eReal);
    }

}
