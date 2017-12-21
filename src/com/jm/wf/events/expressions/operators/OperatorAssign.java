package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.events.expressions.Factor;
import com.jm.wf.events.expressions.LocalVariable;

public class OperatorAssign extends OperatorBase
{

	@Override
	public DataValue evaluate(Object object)
	{
		DataValue rhsValue = getRhs().evaluate(object);

		LocalVariable variable = ((Factor) getLhs()).getVariable();
		
		variable.setValue(rhsValue);
		
		return rhsValue;
	}

}
