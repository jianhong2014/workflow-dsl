package com.jm.wf.events.expressions.operators;

import java.util.HashMap;
import java.util.Map;

import com.jm.wf.events.expressions.ExpressionBase;


public abstract class OperatorBase extends ExpressionBase
{
    private String operatorName = "";
    
	private ExpressionBase lhs = null;
	private ExpressionBase rhs = null;
	
	private static Map<String, Class<?>> opMapping = new HashMap<String, Class<?>>();
	
	private static boolean initialised = false;
	
	
	public static OperatorBase create(String opStr)
	{
	    OperatorBase operator = null;
	
	    if (!initialised)
	    {
	        initialise();
	    }
	    
	    Class<?> opClass = opMapping.get(opStr.toLowerCase());
	    
	    if (opClass != null)
	    {
	        try
	        {
	            operator = (OperatorBase) opClass.newInstance();
	            
	            operator.operatorName = opStr.toLowerCase();
	        }
	        catch (Exception e)
	        {
	            operator = null;
	        }
	    }
	    
	    return operator;
	}
	
	private static void initialise()
	{
        opMapping.put("=",   OperatorAssign.class);
        opMapping.put("&=",  OperatorAssignBitAnd.class);
        opMapping.put("|=",  OperatorAssignBitOr.class);
        opMapping.put("^=",  OperatorAssignBitXor.class);
        opMapping.put("/=",  OperatorAssignDivide.class);
        opMapping.put("<<=", OperatorAssignLShift.class);
        opMapping.put("-=",  OperatorAssignMinus.class);
        opMapping.put("%=",  OperatorAssignModulus.class);
        opMapping.put("*=",  OperatorAssignMultiply.class);
        opMapping.put("+=",  OperatorAssignPlus.class);
        opMapping.put(">>=", OperatorAssignRShift.class);
        opMapping.put("&",   OperatorBitAnd.class);
        opMapping.put("|",   OperatorBitOr.class);
        opMapping.put("^",   OperatorBitXor.class);
        opMapping.put("/",   OperatorDivide.class);
        opMapping.put("<<",  OperatorLeftShift.class);
        opMapping.put("&&",  OperatorLogAnd.class);
        opMapping.put("==",  OperatorLogEq.class);
        opMapping.put(">",   OperatorLogGt.class);
        opMapping.put(">=",  OperatorLogGte.class);
        opMapping.put("<",   OperatorLogLt.class);
        opMapping.put("<=",  OperatorLogLte.class);
        opMapping.put("!=",  OperatorLogNe.class);
        opMapping.put("||",  OperatorLogOr.class);
        opMapping.put("-",   OperatorMinus.class);
        opMapping.put("%",   OperatorModulus.class);
        opMapping.put("*",   OperatorMultiply.class);
        opMapping.put("+",   OperatorPlus.class);
        opMapping.put("**",  OperatorPower.class);
        opMapping.put(">>",  OperatorRightShift.class);
	}

    public synchronized String getOperatorName()
    {
        return operatorName;
    }

    public synchronized void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }
	
	public ExpressionBase getLhs() {
		return lhs;
	}

	public void setLhs(ExpressionBase lhs) {
		this.lhs = lhs;
	}

	public ExpressionBase getRhs() {
		return rhs;
	}

	public void setRhs(ExpressionBase rhs) {
		this.rhs = rhs;
	}
	
	public String toString()
	{
	    StringBuilder str = new StringBuilder();
	    
	    if (lhs != null)
	    {
	        str.append(lhs).append(" ");
	        
	    }
	    
	    str.append(operatorName);
	    
	    if (rhs != null)
	    {
	        if (lhs != null)
	        {
	            str.append(" ");
	        }
	        
	        str.append(rhs);
	    }
	    
	    return str.toString();
	}
}
