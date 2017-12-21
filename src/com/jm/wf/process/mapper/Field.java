package com.jm.wf.process.mapper;

import java.util.ArrayList;
import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.events.expressions.FieldValueExpression;
import com.jm.wf.events.expressions.InvalidExpressionFormatException;
import com.jm.wf.source.service.filters.ApiOperationFilterTemplate;


public class Field
{
	private String name = null;
	private List<FieldValueExpression> valueList = new ArrayList<FieldValueExpression>();
	private String value = null;
		
	public Field()
	{
		super();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public void addValue(String exp)
	{
		FieldValueExpression expValue = null;
		//System.out.println("String exp value = " + exp);
		try 
		{
			expValue = FieldValueExpression.create(ApiOperationFilterTemplate.class,exp);
			//System.out.println("String  value = " + expValue);
			valueList.add(expValue);
		}
		catch (InvalidExpressionFormatException e)
		{
			//System.out.println("InvalidExpressionFormatException ");
			e.printStackTrace();
			LoggerManager.getLogger().error("Field:addValue" + e);
		}		
	}

	public List<FieldValueExpression> getValueList()
	{
		return valueList;
	}
	
	public DataValue getValue(Object apiOperation)
    {
		DataValue val = null;
		// System.out.println("apiOperation request "+valueList);
		int listHit = 0;
		for (FieldValueExpression exp : valueList)
		{
			listHit++;
			//System.out.println("get exp avlue "+exp.getExpressions());
			val = exp.evaluate(apiOperation);
			if (val != null)
			{
				break;
			}
		}
		//logger.debug("Found Value at " + listHit);
		return val;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Field Name = " + name);
		sb.append(" Expression List = " + valueList);
		//sb.append("\n");
		return sb.toString();
	}

}
