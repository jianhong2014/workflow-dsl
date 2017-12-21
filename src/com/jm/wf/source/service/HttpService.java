package com.jm.wf.source.service;

import java.util.HashMap;

import com.sun.javafx.collections.MappingChange.Map;

public class HttpService implements Service{

	private String method = "post";
	private String url = "";
	private String body = "";//json or xml
	public HttpService(String method, String url, String body) {
		super();
		this.method = method;
		this.url = url;
		this.body = body;
	}
	
	public Map<String,Object> call(Object object){
		return (Map<String, Object>) new HashMap<String,Object>();
	}
	
}
