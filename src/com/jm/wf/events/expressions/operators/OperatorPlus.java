package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueDuration;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.datavalues.DataValueReal;
import com.jm.wf.common.datavalues.DataValueString;
import com.jm.wf.common.datavalues.DataValueTimestamp;
import com.jm.wf.common.datavalues.DataValueType;

public class OperatorPlus extends OperatorBase
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
        else if (lhsValue instanceof DataValueString || rhsValue instanceof DataValueString ||
                 lhsValue instanceof DataValueTimestamp || rhsValue instanceof DataValueTimestamp ||
                 lhsValue instanceof DataValueDuration || rhsValue instanceof DataValueDuration)
		{
			return DataValueFactory.createDataValue(lhsValue.toString() + rhsValue.toString(),
					                                DataValueType.eString);
		}
		else if (lhsValue instanceof DataValueReal || rhsValue instanceof DataValueReal)
		{
			return DataValueFactory.createDataValue(lhsValue.toReal() + rhsValue.toReal(),
					                                DataValueType.eReal);
		}

		return DataValueFactory.createDataValue(lhsValue.toInteger() + rhsValue.toInteger(),
                                                DataValueType.eInteger);
	}

}
