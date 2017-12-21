package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueSet extends DataValueCollectionBase
{
    public DataValueSet() throws InvalidDataTypeException
    {
        super(DataValueType.eSet);
        
        values = new HashSet<DataValue>();
    }

    public DataValueSet(Collection<DataValue> values) throws InvalidDataTypeException
    {
        super(DataValueType.eSet);
        
        this.values = new HashSet<DataValue>(values);
    }

    @Override
    public DataValue newInstance()
    {
        DataValueSet[] other = { null };
        
        try
        {
            other[0] = new DataValueSet();
            
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
        return (values.size() != 0);
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
        else if (!(other instanceof DataValueSet))
        {
            return false;
        }
        else
        {
            DataValueSet set = (DataValueSet) other;
            
            return (values.equals(set.values));
        }
    }

    @Override
    public boolean lessThan(Object other)
    {
        if (other == this)
        {
            return false;
        }
        else if (!(other instanceof DataValueSet))
        {
            return false;
        }
        else
        {
            DataValueSet set = (DataValueSet) other;
            
            boolean result = true;
            
            // Check to see if each element in this list is less than the corresponding
            // element in the other list.
            
            Iterator<DataValue> iter1 = values.iterator();
            Iterator<DataValue> iter2 = set.values.iterator();

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
                result = (values.size() <= set.values.size());
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
		UnsignedInts.putUnsignedByte(buffer,0x0F);
		offset=offset+1;
		UnsignedInts.putUnsignedShort(buffer,values.size());
		offset=offset+2;
		System.arraycopy(byteBuffer, 0, buffer.array(), offset, byteBuffer.length);
		return buffer.array();
	}
}
