package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;
import com.jm.wf.events.expressions.Factor;
import com.jm.wf.events.expressions.LocalVariable;

public class MethodForEach extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        LocalVariable       param1 = null;
        DataValueCollection param2 = null;
        ExpressionBase      param3 = null;
        DataValue           value  = null;
        
        boolean first  = true;
        boolean second = false;
        
        for (ExpressionBase expression : getParameters())
        {
            if (first)
            {
                param1 = ((Factor) expression).getVariable();
                
                first  = false;
                second = true;
            }
            else if (second)
            {
                value = expression.evaluate(object);
                
                if (value instanceof DataValueCollection)
                {
                    param2 = (DataValueCollection) value;
                }
                else
                {
                    param2 = (DataValueCollection) DataValueFactory.createDataValue(value,
                                                                                    DataValueType.eList);
                }
                
                second = false;
            }
            else
            {
                param3 = expression;
                
                break;
            }
        }

        DataValueCollection result = 
            (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eList);
        
        if (param1 != null && param2 != null && param3 != null)
        {
            
            for (DataValue member : param2.getValues())
            {
                param1.setValue(member);
                
                value = param3.evaluate(object);
                
                result.add(value);
            }
        }
        else
        {
            value = DataValueFactory.createNullValue();
        }
        
        return (value == null ? (DataValue) result : value);
    }

}
