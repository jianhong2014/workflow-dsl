package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueMapBase extends DataValueBase implements DataValueMap
{
    protected Map<DataValue,DataValue> valueMap = null;

    public DataValueMapBase() throws InvalidDataTypeException
    {
        super(DataValueType.eMap);
        
        valueMap = new HashMap<DataValue,DataValue>();
    }

    public DataValueMapBase(Map<DataValue,DataValue> valueMap) throws InvalidDataTypeException
    {
        super(DataValueType.eMap);
        
        this.valueMap = new HashMap<DataValue,DataValue>(valueMap);
    }

    public DataValueMapBase(Collection<DataValue> values) throws InvalidDataTypeException
    {
        super(DataValueType.eMap);
        
        this.valueMap = new HashMap<DataValue,DataValue>();

        Iterator<DataValue> iter = values.iterator();
        
        DataValue key = null;
        DataValue val = null;
        
        do
        {
            key = null;
            val = null;
            
            if (iter.hasNext())
            {
                key = iter.next();
            }
            
            if (iter.hasNext())
            {
                val = iter.next();
            }
            
            if (key != null && val != null)
            {
                valueMap.put(key, val);
            }
        }
        while (key != null && val != null);
    }

    public long toInteger()
    {
        return size();
    }
    
    public double toReal()
    {
        return size();
    }
    
    public boolean toBoolean()
    {
        return !isEmpty();
    }
    
    public String toUnformattedString()
    {
        return valueMap.toString();
    }
    
    public String toString()
    {
        return toUnformattedString();
    }
    
    public void hashCode(int hc)
    {
        generatedHashCode = hc;
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = valueMap.hashCode();
        }
        return generatedHashCode;
    }
    
    public int size()
    {
        // Most DataValue objects have a size of 1 as indicated by this base class implementation.
        // Lists and sets will return the number of member elements. DataValueNull will return a
        // size of 0.
        
        return valueMap.size();
    }
    
    public int length()
    {
        // Most DataValue objects have a length of 1 as indicated by this base class implementation.
        // Lists and sets will return the number of member elements. Strings will return the number
        // of characters in the string. DataValueNull will return a length of 0.
        
        return valueMap.size();
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        else if (!(other instanceof DataValueMap))
        {
            return false;
        }
        else
        {
            DataValueMapBase map = (DataValueMapBase) other;
            
            return valueMap.equals(map.valueMap);
        }
    }

    @Override
    public boolean lessThan(Object other)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public DataValue newInstance()
    {
        DataValueMapBase[] other = { null };
        
        try
        {
            other[0] = new DataValueMapBase();
            
            for (DataValue value : valueMap.keySet())
            {
                other[0].valueMap.put(value.newInstance(), valueMap.get(value).newInstance());
            }
            
            copy(other);
        }
        catch (InvalidDataTypeException e)
        {
            other[0] = null;
        }
        
        return other[0];
    }

    @Override
    public byte[] asByteArray()
    {
        return null;
    }

    public void clear()
    {
        valueMap.clear();
    }
    
    public boolean containsKey(DataValue key)
    {
        return valueMap.containsKey(key);
    }
    
    public boolean containsValue(DataValue value)
    {
        return valueMap.containsValue(value);
    }
    
    public DataValue get(DataValue key)
    {
        return valueMap.get(key);
    }
    
    public boolean isEmpty()
    {
        return valueMap.isEmpty();
    }
    
    public DataValueCollection keys()
    {
        return (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eSet);
    }
    
    public DataValue put(DataValue key, DataValue value)
    {
        return valueMap.put(key, value);
    }
    
    public void putAll(Map<DataValue,DataValue> map)
    {
        valueMap.putAll(map);
    }
    
    public void putAll(DataValueMap map)
    {
        valueMap.putAll(map.getValueMap());
    }
    
    public void putAll(DataValueCollection keyValueList)
    {
        Iterator<DataValue> iter = keyValueList.getValues().iterator();
        
        DataValue key   = null;
        DataValue value = null;
        
        do
        {
            key   = null;
            value = null;
            
            if (iter.hasNext())
            {
                key = iter.next();
            }
            
            if (iter.hasNext())
            {
                value = iter.next();
            }
            
            if (key != null && value != null)
            {
                valueMap.put(key, value);
            }
        }
        while (key != null && value != null);
    }
    
    public DataValueCollection values()
    {
        return (DataValueCollection) DataValueFactory.createDataValueCollection(DataValueType.eList);
    }
    
    public Map<DataValue,DataValue> getValueMap()
    {
        return valueMap;
    }

    @Override
	public byte[] toFidrBinary() 
	{
		int offset=0;
		byte[] byteBuffer = ByteUtils.mapToBinary(valueMap);
		ByteBuffer buffer=ByteBuffer.allocate(byteBuffer.length + 3);
		UnsignedInts.putUnsignedByte(buffer,0x10);
		offset=offset+1;
		UnsignedInts.putUnsignedShort(buffer,valueMap.keySet().size());
		offset=offset+2;
		System.arraycopy(byteBuffer, 0, buffer.array(), offset, byteBuffer.length);
		return buffer.array();
	}

	
}
