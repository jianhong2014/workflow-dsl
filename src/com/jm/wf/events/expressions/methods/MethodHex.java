package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodHex extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue value = null;
        
        for (ExpressionBase expression : getParameters())
        {
            value = expression.evaluate(object);
            
            if (value == null)
            {
                value = DataValueFactory.createNullValue();
            }
            
            break;
        }
        
        DataValue intVal = DataValueFactory.createDataValue(value.toString(),
                                                            DataValueType.eInteger);

        return DataValueFactory.createDataValue("0x" + Long.toHexString(intVal.toInteger()),
                                                DataValueType.eString);
    }

}
