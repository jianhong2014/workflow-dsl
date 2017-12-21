package com.jm.wf.common.datavalues;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public abstract class DataValueCollectionBase extends DataValueBase implements DataValueCollection
{
    protected Collection<DataValue> values = null;

    public DataValueCollectionBase(DataValueType type) throws InvalidDataTypeException
    {
        super(type);
    }

    public void hashCode(int hc)
    {
        generatedHashCode = hc;
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = values.hashCode();
        }
        return generatedHashCode;
    }
    
    public int size()
    {
        // Most DataValue objects have a size of 1 as indicated by this base class implementation.
        // Lists and sets will return the number of member elements. DataValueNull will return a
        // size of 0.
        
        return values.size();
    }
    
    public int length()
    {
        // Most DataValue objects have a length of 1 as indicated by this base class implementation.
        // Lists and sets will return the number of member elements. Strings will return the number
        // of characters in the string. DataValueNull will return a length of 0.
        
        return values.size();
    }


    public boolean add(DataValue value)
    {
        return values.add(value);
    }
    
    public boolean addAll(List<DataValue> values)
    {
        return this.values.addAll(values);
    }
    
    public boolean addAll(Set<DataValue> values)
    {
        return this.values.addAll(values);
    }
    
    public boolean addAll(DataValueCollection values)
    {
        return this.values.addAll(values.getValues());
    }
    
    public void clear()
    {
        values.clear();
    }
    
    public boolean contains(DataValue value)
    {
        return values.contains(value);
    }
    
    public boolean containsAll(DataValueCollection value)
    {
        return values.containsAll(value.getValues());
    }
    
    public boolean isEmpty()
    {
        return values.isEmpty();
    }
    
    public void setValues(Collection<DataValue> values)
    {
        this.values = values;
    }
    
    public Collection<DataValue> getValues()
    {
        return values;
    }
}
