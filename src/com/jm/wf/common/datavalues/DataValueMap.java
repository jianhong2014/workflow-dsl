package com.jm.wf.common.datavalues;

import java.util.Map;

public interface DataValueMap extends DataValue
{
    public void clear();
    
    public boolean containsKey(DataValue key);
    
    public boolean containsValue(DataValue value);
    
    public DataValue get(DataValue key);
    
    public boolean isEmpty();
    
    public DataValueCollection keys();
    
    public DataValue put(DataValue key, DataValue value);
    
    public void putAll(DataValueMap map);
    
    public void putAll(Map<DataValue,DataValue> map);
    
    public void putAll(DataValueCollection keyValueList);
    
    public DataValueCollection values();
    
    public Map<DataValue,DataValue> getValueMap();
}
