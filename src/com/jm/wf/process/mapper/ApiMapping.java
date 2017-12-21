package com.jm.wf.process.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ApiMapping {
	private String apiId  = "";
	private List<TaskMapping> taskMappings = new ArrayList<TaskMapping>();
	
	//could be used to mapping to different task according to request info
	private List<Decisions> decSwitchList = new ArrayList<Decisions>();
	
	public ApiMapping() {
		
	}
	
	public void addTaskMapping(TaskMapping task)
	{
		//System.out.println("add TaskMapping ");
		taskMappings.add(task);
	}
	
	public List<TaskMapping> getTaskMappings() {
		return taskMappings;
	}

	public void setTaskMappings(List<TaskMapping> taskMappings) {
		this.taskMappings = taskMappings;
	}

	public void addDecSwitch(Decisions ds)
	{
		//System.out.println("add ds in api mapping ");
		decSwitchList.add(ds);
	}
	
	
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public List<Decisions> getDecSwitchList() {
		return decSwitchList;
	}
	public void setDecSwitchList(List<Decisions> decSwitchList) {
		this.decSwitchList = decSwitchList;
	}
	public Map<String,Map<String,Object>> getMappedResult(Object request){
		Map<String,Map<String,Object>> mappedResult = new HashMap<String,Map<String,Object>>();
		for(TaskMapping taskMapping : taskMappings) {
			mappedResult.put(taskMapping.getName(), taskMapping.getValue(request));
			mappedResult = mergeMapResult(getDecisionResult(taskMapping.getDecSwitchList(),request),mappedResult);
		}
		for(Decisions decision : decSwitchList) {
			Decision decCase = decision.getCase(request);
			if(decCase != null) {
				//System.out.println("has one decision matched result ");
				mappedResult = mergeMapResult(getTaskMappingResult(decCase.getTaskMappings(),request),mappedResult);
			}
		}
		
		return mappedResult;
	}
	
	public Map<String,Map<String,Object>> getDecisionResult(List<Decisions> decSwitchList , Object request){
		Map<String,Map<String,Object>> mappedResult = new HashMap<String,Map<String,Object>>();
		for(Decisions decision : decSwitchList) {
			Decision decCase = decision.getCase(request);
			if(decCase != null) {
				mergeMapResult(getTaskMappingResult(decCase.getTaskMappings(),request),mappedResult);
			}
		}
		return mappedResult;
	}
	
	private Map<String,Map<String,Object>> getTaskMappingResult(List<TaskMapping> taskMappings,
			Object request){
		Map<String,Map<String,Object>> mappedResult = new HashMap<String,Map<String,Object>>();
		for(TaskMapping taskMapping : taskMappings) {
			//System.out.println("task mapping "+taskMapping.getName());
			mappedResult.put(taskMapping.getName(), taskMapping.getValue(request));
		}
		return mappedResult;
	}
	
    private Map<String,Map<String,Object>> mergeMapResult(Map<String,Map<String,Object>> from, 
			Map<String,Map<String,Object>> to){
		for(String key : from.keySet()) {
			Map<String,Object> dataInFrom = from.get(key);
			Map<String,Object> dataInTo = from.get(key);
			if(dataInTo != null) {
				dataInTo.putAll(dataInFrom);
			} else {
				dataInTo = dataInFrom;
			}
			to.put(key, dataInTo);	
		}
		
		return to;
		
	}
}
