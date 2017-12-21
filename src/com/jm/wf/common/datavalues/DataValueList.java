package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueList extends DataValueCollectionBase
{
    public DataValueList() throws InvalidDataTypeException
    {
        super(DataValueType.eList);
        
        values = new ArrayList<DataValue>();
    }

    public DataValueList(Collection<DataValue> values) throws InvalidDataTypeException
    {
        super(DataValueType.eList);
        
        this.values = new ArrayList<DataValue>(values);
    }

    @Override
    public DataValue newInstance()
    {
        DataValueList[] other = { null };
        
        try
        {
            other[0] = new DataValueList();
            
            for (DataValue value : values)
            {
                other[0].values.add(value.newInstance());
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
        // TODO Auto-generated method stub
        return null;
    }

    public long toInteger()
    {
        return values.size();
    }
    
    public double toReal()
    {
        return values.size();
    }
    
    public boolean toBoolean()
    {
        return (!isEmpty());
    }
    
    public String toUnformattedString()
    {
        return values.toString();
    }
    
    public String toString()
    {
        return toUnformattedString();
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        else if (!(other instanceof DataValueList))
        {
            return false;
        }
        else
        {
            DataValueList list = (DataValueList) other;
            
            return (values.equals(list.values));
        }
    }

    @Override
    public boolean lessThan(Object other)
    {
        if (other == this)
        {
            return false;
        }
        else if (!(other instanceof DataValueList))
        {
            return false;
        }
        else
        {
            DataValueList list = (DataValueList) other;
            
            boolean result = true;
            
            // Check to see if each element in this list is less than the corresponding
            // element in the other list.
            
            Iterator<DataValue> iter1 = values.iterator();
            Iterator<DataValue> iter2 = list.values.iterator();

            DataValue value1 = null;
            DataValue value2 = null;
            
            while (result && iter1.hasNext() && iter2.hasNext())
            {
                value1 = iter1.next();
                value2 = iter2.next();
                
                result = (value1.lessThan(value2) || value1.equals(value2));
            }
            
            // If the result is still true then the final check is to determine whether
            // this list is no longer than the other list.
            
            if (result)
            {
                result = (values.size() <= list.values.size());
            }
            
            return result;
        }
    }

	@Override
	public byte[] toFidrBinary() 
	{
		int offset=0;
		byte[] byteBuffer = ByteUtils.collectionToBinary(values);
		ByteBuffer buffer=ByteBuffer.allocate(byteBuffer.length + 3);
		UnsignedInts.putUnsignedByte(buffer,0x0E);
		offset=offset+1;
		UnsignedInts.putUnsignedShort(buffer,values.size());
		offset=offset+2;
		System.arraycopy(byteBuffer, 0, buffer.array(), offset, byteBuffer.length);
		return buffer.array();
	}

	
}
