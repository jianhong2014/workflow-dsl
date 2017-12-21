package com.jm.wf.process.mapper;

import java.util.ArrayList;
import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.events.filters.FilterInterval;
import com.jm.wf.events.filters.FilterIntervalBase;
import com.jm.wf.events.filters.InvalidFilterIntervalException;


public class Decision
{
	
	private List<Field> fields = new ArrayList<Field>();
	
	private List<TaskMapping> taskMappings = new ArrayList<TaskMapping>();
	
	private List<Decisions> switchList = new ArrayList<Decisions>();

	private List<FilterInterval> intervalList = null;
	
	private DataValueType intervalValueType = DataValueType.eString;
	
	private String strCaseVal = null;
	
	private boolean defaultCase = false;
	
	public Decision()
	{
		super();
	}

	public List<Field> getFields()
	{
		return fields;
	}

	public List<Decisions> getSwitchList()
	{
		return switchList;
	}
	
	public void addField(Field field)
	{
		//System.out.println("add field "+field);
		fields.add(field);
	}
	
	public void addSwitch(Decisions ds)
	{
		switchList.add(ds);
	}
	public void addTaskMapping(TaskMapping task)
	{
		// System.out.println("add task mapping "+task);;
		taskMappings.add(task);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(" Decision Case = " + strCaseVal);
		sb.append(" fieldList = " + fields);
		sb.append(" SwitchCaseList = " + switchList);
		
		return sb.toString();
	}

	public List<FilterInterval> getIntervalList()
	{
		return intervalList;
	}

	public void setInterval() throws InvalidFilterIntervalException
	{	
		intervalList = FilterIntervalBase.createList(strCaseVal,intervalValueType);
	}

	public DataValueType getIntervalValueType()
	{
		return intervalValueType;
	}

	public void setIntervalValueType(DataValueType intervalValueType)
	{
		this.intervalValueType = intervalValueType;
	}

	public String getStrCaseVal()
	{
		return strCaseVal;
	}

	public List<TaskMapping> getTaskMappings() {
		return taskMappings;
	}

	public void setTaskMappings(List<TaskMapping> taskMappings) {
		this.taskMappings = taskMappings;
	}

	public void setStrCaseVal(String strCaseVal)
	{
		this.strCaseVal = strCaseVal;
		if (strCaseVal.equalsIgnoreCase("Default"))
		{
			defaultCase = true;
		}
	}
	
	public boolean matches(DataValue value)
	{
		boolean match = false;
		
		for(FilterInterval interval : intervalList)
		{
			if (interval.matches(value))
			{
				match = true;
				break;
			}
		}
		
		return match;
	}

	public boolean isDefaultCase() 
	{
		return defaultCase;
	}
}
