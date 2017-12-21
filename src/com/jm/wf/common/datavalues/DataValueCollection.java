package com.jm.wf.common.datavalues;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DataValueCollection
{
    public boolean add(DataValue value);
    
    public boolean addAll(List<DataValue> values);
    
    public boolean addAll(Set<DataValue> values);
    
    public boolean addAll(DataValueCollection values);
    
    public void clear();
    
    public boolean contains(DataValue value);

    public boolean containsAll(DataValueCollection value);
    
    public void setValues(Collection<DataValue> values);
    
    public Collection<DataValue> getValues();
    
    public boolean isEmpty();
}
