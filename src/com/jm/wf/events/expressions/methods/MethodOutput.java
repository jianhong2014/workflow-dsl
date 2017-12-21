package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodOutput extends MethodBase
{
    private String fmtStr = null;

    @Override
    public DataValue evaluate(Object object)
    {
        DataValue value  = null;
        
        boolean   first  = true;
        
        for (ExpressionBase expression : getParameters())
        {
            if (first)
            {
                if (fmtStr == null)
                {
                    value = expression.evaluate(object);
                    
                    fmtStr = value.toString();
                }
                
                first = false;
            }
            else
            {
                value = expression.evaluate(object);
                
                if (value != null)
                {
                    value.setFormatString(fmtStr);
                }
                
                break;
            }
        }
        
        if (value == null)
        {
            value = DataValueFactory.createNullValue();
        }
        
        return value;
    }

}
