package com.jm.wf.process.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskMapping {
	
	private String name = "";
	private List<Field> fields = new ArrayList<Field>();
	private List<Decisions> decSwitchList = new ArrayList<Decisions>();
	
	public void addField(Field field)
	{
		fields.add(field);
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Decisions> getDecSwitchList() {
		return decSwitchList;
	}
	public void setDecSwitchList(List<Decisions> decSwitchList) {
		this.decSwitchList = decSwitchList;
	}
	public void addDecSwitch(Decisions ds)
	{
		decSwitchList.add(ds);
	}
	
	public Map<String,Object> getValue(Object request){
		Map<String,Object> mappedResult = new HashMap<String,Object>();
		for(Field field : fields) {
			mappedResult.put(field.getName(), field.getValue(request));
		}
		for(Decisions decision : decSwitchList) {
			Decision decCase = decision.getCase(request);
			if(decCase != null) {
				//System.out.println("matched deacde in task mapping decision "+decCase.getFields());
				for(Field field : decCase.getFields()) {
					mappedResult.put(field.getName(), field.getValue(request));
				}
			}
		}
		
		return mappedResult;
	}
}
