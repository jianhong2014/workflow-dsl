package com.jm.wf.source.service;

import com.sun.javafx.collections.MappingChange.Map;

public interface Service {
	public Map<String,Object> call(Object object);
}
