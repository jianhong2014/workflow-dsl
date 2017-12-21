package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodAvg extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        double result = 0.0;
        long   count  = 0;
        
        for (ExpressionBase expression : getParameters())
        {
            DataValue value = expression.evaluate(object);
            
            if (value != null && value instanceof DataValueCollection)
            {
                for (DataValue member : ((DataValueCollection) value).getValues())
                {
                    result += member.toReal();
                    count++;
                }
            }
            else if (value != null)
            {
                result += value.toReal();
                count++;
            }
        }

        return DataValueFactory.createDataValue(count != 0 ? (result / count) : Double.NaN,
                                                DataValueType.eReal);
    }

}
