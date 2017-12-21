package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.expressions.ExpressionBase;

public class MethodLargeThan extends MethodBase {

	@Override
	public DataValue evaluate(Object object) {
		DataValue value = null;
		int length = getParameters().size();
		if (length == 2) {
			ExpressionBase firstExpr = getParameters().get(0);
			ExpressionBase secondExpr = getParameters().get(1);
			if (firstExpr.evaluate(object).toInteger() > secondExpr.evaluate(object).toInteger()) {
				value = DataValueFactory.createDataValue("true", DataValueType.eBoolean);
			} else {
				value = DataValueFactory.createDataValue("false", DataValueType.eBoolean);
			}
		}

		if (value == null) {
			value = DataValueFactory.createNullValue();
		}

		return value;
	}

}
