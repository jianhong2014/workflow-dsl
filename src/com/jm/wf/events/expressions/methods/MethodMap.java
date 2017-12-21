package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueMap;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodMap extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValueMap map =
            (DataValueMap) DataValueFactory.createDataValueCollection(DataValueType.eMap);
        
        DataValueCollection list =
            (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eList);
        
        for (ExpressionBase expression : getParameters())
        {
            DataValue value = expression.evaluate(object);
            
            if (value != null)
            {
                list.add(value);
            }
        }
        
        if (!list.isEmpty())
        {
            map.putAll(list);
        }
        
        return (DataValue) map;
    }

}
