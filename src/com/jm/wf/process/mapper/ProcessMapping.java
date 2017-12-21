package com.jm.wf.process.mapper;


import java.util.HashMap;
import java.util.Map;

public class ProcessMapping {
	
	
    private String process = "";
	private Map<String,ApiMapping> apiMappings = new HashMap<String,ApiMapping>();
	
	public void addApiMapping(ApiMapping apiMapping) {
		//System.out.println("add apiMappings ");
		this.apiMappings.put(apiMapping.getApiId(),apiMapping);
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Map<String,Map<String,Object>>  getApiMapping(String apiId, Object request){
		Map<String,Map<String,Object>> mappedResult = new HashMap<String,Map<String,Object>>();
		ApiMapping apiMapping = apiMappings.get(apiId);
		if(apiMapping != null) {
			mappedResult = apiMapping.getMappedResult(request);
		}
		return mappedResult;
	}

	@Override
	public String toString() {
		return "ProcessMapping [process=" + process + ", apiMappings=" + apiMappings + "]";
	}
	
   
   
}
