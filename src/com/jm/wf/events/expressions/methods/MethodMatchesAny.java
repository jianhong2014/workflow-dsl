package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;
import com.jm.wf.events.expressions.Factor;
import com.jm.wf.events.filters.FilterTemplate;

public class MethodMatchesAny extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        boolean result = true;
        
        for (ExpressionBase expression : getParameters())
        {
            FilterTemplate template = null;
            
            if ((expression instanceof Factor) &&
                ((template = ((Factor) expression).getTemplate()) != null))
            {
                result = template.matches(object);
            }
            else
            {
                DataValue value = expression.evaluate(object);
                
                result = ((value != null) && value.toBoolean()); 
            }
            
            if (result)
                break;
        }

        return DataValueFactory.createDataValue(result ? 1 : 0,
                                                DataValueType.eBoolean);
    }

}
