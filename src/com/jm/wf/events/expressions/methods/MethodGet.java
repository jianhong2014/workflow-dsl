package com.jm.wf.events.expressions.methods;

import java.util.Collection;
import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueMap;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;
import com.jm.wf.common.datavalues.DataValueList;
import com.jm.wf.common.datavalues.DataValueSet;
import com.jm.wf.common.logging.LoggerManager;

public class MethodGet extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        DataValue param1 = null;
        DataValue param2 = null;
        DataValue result = null;

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
            DataValueMap        map  = null;

            if (param1 instanceof DataValueCollection)
            {
            	if (param1 instanceof DataValueList)
            	{
            		list = (DataValueCollection) param1;
            	}
            	else //DataValueSet
            	{
            		try
            		{
            			Collection<DataValue> setElements = ((DataValueSet)param1).getValues();
            			list = new DataValueList(setElements);
            		}
            		catch (Exception e)
            		{
            			LoggerManager.getLogger().error("MethodGet: " + e);
            		}
            	}
            }
            else if (param1 instanceof DataValueMap)
            {
                map = (DataValueMap) param1;
            }
            else
            {
                list = (DataValueCollection) DataValueFactory.createDataValue(param1,
                                                                              DataValueType.eList);
            }
            
            if (list != null)
            {
                int index = (int) param2.toInteger();

                if (index >= 0 && index < ((DataValue) list).size())
                {
                    result = ((List<DataValue>) list.getValues()).get(index);
                }
                else
                {
                    result = DataValueFactory.createNullValue();
                }
            }
            else if (map != null)
            {
                result = map.get(param2);
            }
            else
            {
                result = DataValueFactory.createNullValue();
            }
            
            if (result == null)
            {
                result = DataValueFactory.createNullValue();
            }
        }
        else
        {
            result = DataValueFactory.createNullValue();
        }

        return DataValueFactory.createDataValue(result,
                                                result.getDataValueType());
    }

}
