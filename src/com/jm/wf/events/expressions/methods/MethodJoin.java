package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodJoin extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue param1 = null;
        DataValue param2 = null;

        StringBuilder result = new StringBuilder();
        
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
            
            String joinStr = param2.toString();
            
            first = true;
            
            for (DataValue value : list.getValues())
            {
                if (!first)
                {
                    result.append(joinStr);
                }
                
                first = false;
                
                result.append(value.toString());
            }
        }

        return DataValueFactory.createDataValue(result.toString(),
                                                DataValueType.eString);
    }

}
