package com.jm.wf.events.expressions;

import com.jm.wf.common.datavalues.DataValue;

public class Expression extends ExpressionBase
{
	ExpressionBase expression = null;

	@Override
	public DataValue evaluate(Object object)
	{
		if (expression != null)
		{
			return expression.evaluate(object);
		}
		
		return null;
	}

	public ExpressionBase getSubExpression() {
		return expression;
	}

	public void setSubExpression(ExpressionBase expression) {
		this.expression = expression;
	}
	
	public String toString()
	{
	    StringBuilder str = new StringBuilder();
	    
	    str.append("(").append(expression).append(")");
	    
	    return str.toString();
	}

}
