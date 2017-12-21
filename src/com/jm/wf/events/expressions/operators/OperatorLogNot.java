package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueType;

public class OperatorLogNot extends OperatorBase
{
    public OperatorLogNot()
    {
        setOperatorName("!");
    }

	@Override
	public DataValue evaluate(Object object)
	{
		DataValue rhsValue = getRhs().evaluate(object);

        if (rhsValue == null ||
            rhsValue instanceof DataValueNull)
        {
            return DataValueFactory.createNullValue();
        }
                    
		return DataValueFactory.createDataValue(rhsValue.toBoolean() ? 0 : 1,
                                                DataValueType.eBoolean);
	}

}
