package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueDuration;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueReal;
import com.jm.wf.common.datavalues.DataValueTimestamp;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.Factor;
import com.jm.wf.events.expressions.LocalVariable;

public class OperatorAssignMultiply extends OperatorBase
{

	@Override
	public DataValue evaluate(Object object)
	{
		LocalVariable variable = ((Factor) getLhs()).getVariable();
		
        DataValue lhsValue = variable.getValue();
        DataValue rhsValue = getRhs().evaluate(object);

        if (rhsValue == null || rhsValue instanceof DataValueNull)
        {
            lhsValue = DataValueFactory.createNullValue();
        }
        else if (lhsValue instanceof DataValueReal || rhsValue instanceof DataValueReal ||
                 lhsValue instanceof DataValueTimestamp || rhsValue instanceof DataValueTimestamp ||
                 lhsValue instanceof DataValueDuration || rhsValue instanceof DataValueDuration)
        {
            lhsValue = DataValueFactory.createDataValue(lhsValue.toReal() * rhsValue.toReal(),
                                                        DataValueType.eReal);
        }
        else
        {
            lhsValue = DataValueFactory.createDataValue(lhsValue.toInteger() * rhsValue.toInteger(),
                                                        DataValueType.eInteger);
        }
        
		variable.setValue(lhsValue);
		
		return lhsValue;
	}

}
