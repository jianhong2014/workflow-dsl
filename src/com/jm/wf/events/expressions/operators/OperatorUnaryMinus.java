package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueReal;
import com.jm.wf.common.datavalues.DataValueType;

public class OperatorUnaryMinus extends OperatorBase
{
    public OperatorUnaryMinus()
    {
        setOperatorName("-");
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
        else if (rhsValue instanceof DataValueReal)
		{
			return DataValueFactory.createDataValue(-rhsValue.toReal(),
					                                DataValueType.eReal);
		}

		return DataValueFactory.createDataValue(-rhsValue.toInteger(),
                                                DataValueType.eInteger);
	}

}
