package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodMax extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        double result = Double.NaN;
        
        for (ExpressionBase expression : getParameters())
        {
            DataValue value = expression.evaluate(object);
            
            if (value != null && value instanceof DataValueCollection)
            {
                for (DataValue member : ((DataValueCollection) value).getValues())
                {
                    if ((new Double(result)).isNaN() || member.toReal() > result)
                    {
                        result = member.toReal();
                    }
                }
            }
            else if (value != null)
            {
               if ((new Double(result)).isNaN() || value.toReal() > result)
                {
                    result = value.toReal();
                }
            }
        }

        return DataValueFactory.createDataValue(result,
                                                DataValueType.eReal);
    }

}
