package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodSet extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValueCollection set =
            (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eSet);
        
        for (ExpressionBase expression : getParameters())
        {
            DataValue value = expression.evaluate(object);
            
            if (value != null)
            {
                set.add(value);
            }
        }

        return (DataValue) set;
    }

}
