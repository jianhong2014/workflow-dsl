package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueType;

public class OperatorBitNot extends OperatorBase
{
    public OperatorBitNot()
    {
        setOperatorName("~");
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
            
		return DataValueFactory.createDataValue(~(rhsValue.toInteger()),
                                                DataValueType.eInteger);
	}

}
