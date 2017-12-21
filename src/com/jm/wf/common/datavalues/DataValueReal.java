package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.utils.UnsignedInts;


public class DataValueReal extends DataValueBase
{
    protected double realValue = 0.0;
    
    public DataValueReal() throws InvalidDataTypeException
    {
        super(DataValueType.eReal);
    }

    public DataValueReal(DataValueType type,
                         double        value)
    throws InvalidDataTypeException
    {
        super(type);
        
        realValue = value;
    }
    
    public DataValueReal(DataValueType type,
                         long          value) throws InvalidDataTypeException
    {
        super(type);
        
        realValue = value;
    }
    
    public DataValueReal(DataValueType type,
                         String        value) throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            realValue = new Double(value);
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException(value + " is not a valid " + type);
        }
    }
    
    public DataValue newInstance()
    {
        DataValueReal[] other = { null };
        
        try
        {
            other[0] = new DataValueReal(DataValueType.eReal, realValue);
            
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
        return new Double(realValue).longValue();
    }
    
    public double toReal()
    {
        return realValue;
    }
    
    public boolean toBoolean()
    {
        return (realValue != 0.0);
    }
    
    public String toUnformattedString()
    {
        return new Double(realValue).toString();
    }
    
    public String toString()
    {
        if (formatString.length() == 0)
            return toUnformattedString();
        else
            return String.format(formatString, realValue);
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) realValue;
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
            return realValue == ((DataValue) other).toReal();
    }
    
    public boolean lessThan(Object other)
    {
        return realValue < ((DataValue) other).toReal();
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{

		//allocate buffer 8 bytes for real and 1 for datatype
		ByteBuffer buffer = ByteBuffer.allocate(9);

		UnsignedInts.putUnsignedByte(buffer, 0x02);
		
		buffer.putDouble(realValue);
		
		return buffer.array();

	}

	
}
