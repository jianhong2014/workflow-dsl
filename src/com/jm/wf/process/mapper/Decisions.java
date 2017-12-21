package com.jm.wf.process.mapper;

import java.util.ArrayList;
import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.events.expressions.FieldValueExpression;
import com.jm.wf.events.expressions.InvalidExpressionFormatException;
import com.jm.wf.events.filters.FilterTemplate;
import com.jm.wf.events.filters.InvalidFilterIntervalException;
import com.jm.wf.source.service.filters.ApiOperationFilterTemplate;


public class Decisions
{
	private FilterTemplate switchName = null;
	
	private FieldValueExpression fieldExp = null;
	
	private DataValueType valueType = DataValueType.eString;
	
	private String decName = null;
	
	private List<Decision> caseList = new ArrayList<Decision>();
	
	
	private Decision defaultDecision = null;
	
	public Decisions()
	{
		super();
	}

	public FilterTemplate getSwitchName()
	{
		return switchName;
	}

	public void setSwitchName(String exp)
	{
		//System.out.println("String switch value1=  " + exp);
		try 
		{
			fieldExp = FieldValueExpression.create(ApiOperationFilterTemplate.class,exp);
		}
		catch (InvalidExpressionFormatException e)
		{
			//System.out.println("InvalidExpressionFormatException ");
			e.printStackTrace();
			LoggerManager.getLogger().error("Field:addValue" + e);
		}		
	}

	public void addCase(Decision dc)
	{
		//System.out.println("add case ");
		if (dc.isDefaultCase())
		{
			defaultDecision = dc;
		}
		else
		{
			try
			{
				dc.setIntervalValueType(valueType);
				dc.setInterval();
				caseList.add(dc);
			}
			catch (InvalidFilterIntervalException e)
			{
				LoggerManager.getLogger().error(e);
			}
		}
	}
	
	public Decision getCase(Object request)
	{
		Decision decision = null;
		
		DataValue val = fieldExp.evaluate(request);
		//System.out.println("switch case value "+val+",value type "+val.getDataValueType()+"");
		
		if (val != null)
		{
			for (Decision dec : caseList)
			{
				//System.out.println("match "+dec);
				if (dec.matches(val))
				{
					//System.out.println("matched "+dec);
					decision = dec;
					break;
				}
			}
		}
		//ReCheck instead of else as caseList may not return a match 
		if (decision == null)
		{
			decision = defaultDecision;
		}
		
		return decision;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nSwitchname = " + switchName);
		sb.append("\n\tcaseList = " + caseList);
		
		return sb.toString();
	}

	public String getDecName()
	{
		return decName;
	}

	public void setDecName(String decName)
	{
		this.decName = decName;
		setSwitchName(decName);
	}

	public List<Decision> getCaseList()
	{
		return caseList;
	}

	public void setCaseList(List<Decision> caseList)
	{
		this.caseList = caseList;
	}
}
