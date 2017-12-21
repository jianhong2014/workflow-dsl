package com.jm.wf.events.expressions;

import com.jm.wf.common.datavalues.DataValue;

public abstract class ExpressionBase
{
	public abstract DataValue evaluate(Object object);

}
