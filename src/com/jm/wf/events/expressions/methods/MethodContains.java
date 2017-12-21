package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueMap;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodContains extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue param1 = null;
        DataValue param2 = null;
        boolean   result = false;

        boolean first = true;
        
        for (ExpressionBase expression : getParameters())
        {
            if (first)
            {
                param1 = expression.evaluate(object);
                
                first = false;
            }
            else
            {
                param2 = expression.evaluate(object);

                break;
            }
        }
        
        if (param1 != null && param2 != null)
        {
            DataValueCollection list = null;

            if (param1 instanceof DataValueCollection)
            {
                list = (DataValueCollection) param1;
            }
            else
            {
                list = (DataValueCollection) DataValueFactory.createDataValue(param1,
                                                                              DataValueType.eList);
            }
            
            if (param2 instanceof DataValueCollection)
            {
                result = list.containsAll((DataValueCollection) param2);
            }
            else if (param2 instanceof DataValueMap)
            {
                DataValueCollection collection =
                    (DataValueCollection) DataValueFactory.createDataValue(param2,
                                                                           DataValueType.eList);
                
                result = list.containsAll(collection);
            }
            else
            {
                result = list.contains(param2);
            }
       }

        return DataValueFactory.createDataValue(result ? 1 : 0,
                                                DataValueType.eBoolean);
    }

}
