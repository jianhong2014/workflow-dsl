package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.common.utils.TimeStrings;
import com.jm.wf.common.utils.UnsignedInts;

public class DataValueDuration extends DataValueBase
{
    // The difference between 2 points in time expressed as seconds and nanoseconds.
    
    protected long seconds     = 0;
    protected long nanoSeconds = 0;

    public DataValueDuration() throws InvalidDataTypeException
    {
        super(DataValueType.eDuration);
    }
    
    // Takes an integer number of seconds and stores them as a duration.
    
    public DataValueDuration(DataValueType type, long value)
        throws InvalidDataTypeException
    {
        super(type);
        
        seconds     = value;
        nanoSeconds = 0;
    }
    
    public DataValueDuration(DataValueType type, long value1, long value2)
        throws InvalidDataTypeException
    {
        super(type);
        
        seconds     = value1;
        nanoSeconds = value2;
    }
    
    // Takes a floating point value of seconds and stores them as a duration.
    
    public DataValueDuration(DataValueType type, double value)
        throws InvalidDataTypeException
    {
        super(type);
        
        seconds = new Double(value).longValue();
        
        nanoSeconds = new Double((value - seconds) * 1000000000).longValue();
    }
    
    // Takes a string and interprets it as a duration. If it contains x:y format
    // then it is interpreted as seconds:nanoseconds. Otherwise it is passed to the
    // Double() constructor and treated as a possibly fractional number of seconds.
    // A duration may also be specified using the ms, s, m, h, d and w modifiers that
    // are used in configuration files.
    
    public DataValueDuration(DataValueType type, String value)
        throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            String [] subStrs;

            subStrs = value.split(":", -1);

            if (subStrs.length == 2)
            {
                seconds = Long.decode(subStrs[0]);

                nanoSeconds = Long.decode(subStrs[1]);
            }
            else
            {
                double realValue = 0.0;
                
                realValue = TimeStrings.convertToSeconds(value);
                
                // realValue = new Double(value).doubleValue();
                
                seconds = new Double(realValue).longValue();

                nanoSeconds = new Double((realValue - seconds) * 1000000000).longValue();
            }
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException(value + " is not a valid " + type);
        }
    }
    
    public DataValue newInstance()
    {
        DataValueDuration[] other = { null };
        
        try
        {
            other[0] = new DataValueDuration(DataValueType.eDuration, seconds, nanoSeconds);
            
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
        return seconds;
    }
    
    public double toReal()
    {
        return (double) (seconds + ((double) nanoSeconds / 1000000000));
    }
    
    public boolean toBoolean()
    {
        return (!(seconds == 0 && nanoSeconds == 0));
    }
    
    public String toUnformattedString()
    {
        StringBuilder str = new StringBuilder();
        
        str.append(seconds).append(':').append(nanoSeconds);
        
        return str.toString();  // String.format("%d:%d", seconds, nanoSeconds);
    }
    
    public String toString()
    {
        return toString(false);
    }
    
    public String toString(boolean asHex)
    {
        if (asHex)
            return String.format("0x%08x:0x%08x", seconds, nanoSeconds);
        else
        {
            if (formatString.length() == 0)
            {
                return toUnformattedString();
            }
            else
            {
                return String.format(formatString, (double) (seconds + ((double) nanoSeconds / 1000000000)));
            }
        }
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) (seconds ^ nanoSeconds);
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
            return toReal() == ((DataValue) other).toReal();
    }
    
    public boolean lessThan(Object other)
    {
        return toReal() < ((DataValue) other).toReal();
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		//allocate buffer 8 bytes for real and 1 for datatype
		
		//This may result from a FieldValueExpression of (Smaller Num - Larger Num)
		if (nanoSeconds < 0)
		{
			nanoSeconds = -(nanoSeconds);
		}
		else if (seconds < 0)
		{
			seconds = -(seconds);
		}
		
		String s=seconds+"."+nanoSeconds;		
		ByteBuffer buffer = ByteBuffer.allocate(9);
		UnsignedInts.putUnsignedByte(buffer, 0x06);
		try
		{
			buffer.putDouble(new Double(s).doubleValue());
		}
		catch(NumberFormatException ne)
		{
			LoggerManager.getLogger().info("NumberFormatException " + ne);
			buffer.putDouble(new Double(0).doubleValue());
		}
		return buffer.array();

	}
}
