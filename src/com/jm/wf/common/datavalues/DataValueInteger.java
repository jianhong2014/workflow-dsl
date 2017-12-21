package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.utils.UnsignedInts;


public class DataValueInteger extends DataValueBase
{
    protected long longValue = 0;
    
    public DataValueInteger() throws InvalidDataTypeException
    {
        super(DataValueType.eInteger);
    }
    
    public DataValueInteger(DataValueType type,
                            long          value) throws InvalidDataTypeException
    {
        super(type);

        longValue = value;
    }

    public DataValueInteger(DataValueType type,
                            double        value) throws InvalidDataTypeException
    {
        super(type);
    
        longValue = new Double(value).longValue();
    }

    public DataValueInteger(DataValueType type,
                            String        value) throws InvalidDataTypeException
    {
        super(type);
        
        if (value == null || value.length() == 0)
        {
            value = "0";
        }
        
        try
        {
            longValue = Long.decode(value);
        }
        catch (Exception e)
        {
            try
            {
                longValue = new Double(value).longValue();
            }
            catch (Exception e2)
            {
                throw new InvalidDataTypeException(value + " is not a valid " + type);
            }
        }
    }
    
    public DataValue newInstance()
    {
        DataValueInteger[] other = { null };
        
        try
        {
            other[0] = new DataValueInteger(DataValueType.eInteger, longValue);
            
            copy(other);
        }
        catch (InvalidDataTypeException e)
        {
            other[0] = null;
        }
        
        return other[0];
    }
    
    public long toInteger()
    {
        return longValue;
    }
    
    public double toReal()
    {
        try
        {
            return new Double(longValue);
        }
        catch (Exception e)
        {
            return 0.0;
        }
    }
    
    public boolean toBoolean()
    {
        return (longValue != 0);
    }
    
    public String toUnformattedString()
    {
        return new Long(longValue).toString();
    }
    
    public String toString()
    {
        if (formatString.length() == 0)
            return toUnformattedString();
        else
            return String.format(formatString, longValue);
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) longValue;
        }
        return generatedHashCode;
    }
    
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else
            return longValue == ((DataValue) other).toInteger();
    }
    
    public boolean lessThan(Object other)
    {
        return longValue < ((DataValue) other).toInteger();
    }
    
    public byte [] asByteArray()
    {
        return null;
    }
    
    
	public byte[] toFidrBinaryForCollection() 
	{
		ByteBuffer buffer = null;
		buffer = ByteBuffer.allocate(9);
		UnsignedInts.putUnsignedByte(buffer, 0x01);
		UnsignedInts.putUnsignedLong(buffer,longValue);
		return buffer.array();
	}
    

	@Override
	public byte[] toFidrBinary() 
	{
		
		ByteBuffer buffer =null;
		int len=0;
		if(longValue<Byte.MAX_VALUE)
		{
			len=1;
			buffer= ByteBuffer.allocate(len + 2);
			UnsignedInts.putUnsignedByte(buffer, 0x01);
			UnsignedInts.putUnsignedByte(buffer, len);
			UnsignedInts.putUnsignedByte(buffer,(int)longValue);
		}
		else if(longValue<Short.MAX_VALUE)
		{
			len=2;
			buffer= ByteBuffer.allocate(len + 2);
			UnsignedInts.putUnsignedByte(buffer, 0x01);
			UnsignedInts.putUnsignedByte(buffer, len);
			UnsignedInts.putUnsignedShort(buffer,(int)longValue);
		}
		else if(longValue<Integer.MAX_VALUE)
		{
			len=4;
			buffer= ByteBuffer.allocate(len + 2);
			UnsignedInts.putUnsignedByte(buffer, 0x01);
			UnsignedInts.putUnsignedByte(buffer, len);
			UnsignedInts.putUnsignedInt(buffer,(int)longValue);
		}
		else if(longValue<Long.MAX_VALUE)
		{
			len=8;
			buffer= ByteBuffer.allocate(len + 2);
			UnsignedInts.putUnsignedByte(buffer, 0x01);
			UnsignedInts.putUnsignedByte(buffer, len);
			UnsignedInts.putUnsignedLong(buffer,longValue);
		}
		
		return buffer.array();
	}
}
