package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.Factor;
import com.jm.wf.events.expressions.LocalVariable;

public class OperatorAssignBitAnd extends OperatorBase
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
        else
        {
            lhsValue = DataValueFactory.createDataValue(lhsValue.toInteger() & rhsValue.toInteger(),
                                                        DataValueType.eInteger);
        }
        
        variable.setValue(lhsValue);
		
		return lhsValue;
	}

}
