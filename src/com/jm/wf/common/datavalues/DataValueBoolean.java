package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.utils.UnsignedInts;


public class DataValueBoolean extends DataValueBase
{
    protected boolean boolValue = false;
    
    public DataValueBoolean() throws InvalidDataTypeException
    {
        super(DataValueType.eBoolean);
    }
    
    public DataValueBoolean(DataValueType type,
                            boolean       value)
        throws InvalidDataTypeException
    {
        super(type);
        
        boolValue = value;
    }
    
    public DataValueBoolean(DataValueType type,
                            String        value)
        throws InvalidDataTypeException
    {
        super(type);
        
        if (value.equalsIgnoreCase("true") ||
            value.equalsIgnoreCase("yes") ||
            value.equalsIgnoreCase("on") ||
            value.equalsIgnoreCase("1"))
        {
            boolValue = true;
        }
        else
        {
            boolValue = new Boolean(value);
        }
    }
    
    public DataValue newInstance()
    {
        DataValueBoolean[] other = { null };
        
        try
        {
            other[0] = new DataValueBoolean(DataValueType.eBoolean, boolValue);
            
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
        return (boolValue ? 1 : 0);
    }
    
    public double toReal()
    {
        return (boolValue ? 1.0 : 0.0);
    }
    
    public boolean toBoolean()
    {
        return boolValue;
    }
    
    public String toUnformattedString()
    {
        return new Boolean(boolValue).toString();
    }
    
    public String toString()
    {
        return toUnformattedString();
    }
    
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else
            return toInteger() == ((DataValue) other).toInteger();
    }
    
    public boolean lessThan(Object other)
    {
        return toInteger() < ((DataValue) other).toInteger();
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		int booleanValue=(boolValue?0x01:0x00);
	    ByteBuffer buffer = null;
		buffer = ByteBuffer.allocate(2);
		UnsignedInts.putUnsignedByte(buffer, 0x03);
		UnsignedInts.putUnsignedByte(buffer,booleanValue);
		return buffer.array();
	}
}
