package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueMap;
import com.jm.wf.common.utils.TimeStrings;
import com.jm.wf.events.expressions.ExpressionBase;
import com.jm.wf.events.expressions.Factor;
import com.jm.wf.events.filters.FilterTemplate;

public class MethodTruncInterval extends MethodBase
{
    private String truncStr   = null;
    private int    truncIntvl = 0;
    
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue value  = null;
        
        boolean   first  = true;
        
        for (ExpressionBase expression : getParameters())
        {
            if (first)
            {
                if (truncStr == null)
                {
                    value = expression.evaluate(object);
                    
                    truncStr = value.toString();
                    
                    truncIntvl = (int) TimeStrings.convertToSeconds(truncStr);
                }
                
                first = false;
            }
            else
            {
                FilterTemplate template = null;
                
                if ((expression instanceof Factor) &&
                    ((template = ((Factor) expression).getTemplate()) != null))
                {
                    template.setTruncationInterval(truncIntvl);
                    
                    value = expression.evaluate(object);
                }
                else
                {
                    value = expression.evaluate(object);

                    if (value != null)
                    {
                        if (value instanceof DataValueCollection)
                        {
                            for (DataValue member : ((DataValueCollection) value).getValues())
                            {
                                member.setTruncationInterval(truncIntvl);
                            }
                        }
                        else if (value instanceof DataValueMap)
                        {
                            for (DataValue member : ((DataValueMap) value).values().getValues())
                            {
                                member.setTruncationInterval(truncIntvl);
                            }
                        }
                        else
                        {
                            value.setTruncationInterval(truncIntvl);
                        }
                    }
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
