package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodSplit extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue param1 = null;
        DataValue param2 = null;
        DataValue param3 = null;
        DataValue value  = null;
        
        boolean first  = true;
        boolean second = false;
        
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

        DataValueCollection list =
            (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eList);
        
        if (param1 != null && param2 != null)
        {
            String[] parts = null;
            
            if (param3 == null)
            {
                parts = param1.toString().split(param2.toString());
            }
            else
            {
                parts = param1.toString().split(param2.toString(), (int) param3.toInteger());
            }
            
            for (String str : parts)
            {
                list.add(DataValueFactory.createDataValue(str, DataValueType.eString));
            }
        }
        else
        {
            value = DataValueFactory.createNullValue();
        }
        
        return (value == null ? (DataValue) list : value);
    }

}
