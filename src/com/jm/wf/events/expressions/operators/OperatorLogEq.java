package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueType;

public class OperatorLogEq extends OperatorBase
{

	@Override
	public DataValue evaluate(Object object)
	{
		DataValue lhsValue = getLhs().evaluate(object);
		DataValue rhsValue = getRhs().evaluate(object);

        if (lhsValue == null ||
            rhsValue == null ||
            lhsValue instanceof DataValueNull ||
            rhsValue instanceof DataValueNull)
        {
            return DataValueFactory.createNullValue();
        }
                
		return DataValueFactory.createDataValue(lhsValue.equals(rhsValue) ? 1 : 0,
                                                DataValueType.eBoolean);
	}

}
