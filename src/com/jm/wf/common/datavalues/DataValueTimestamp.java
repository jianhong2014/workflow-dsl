package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.TimeZone;

import com.jm.wf.common.utils.UnsignedInts;

import java.util.Calendar;

public class DataValueTimestamp extends DataValueBase
{
    // A UTC time represented as an offset in seconds and nano seconds from the epoch.
    
    protected long seconds     = 0;
    protected long nanoSeconds = 0;
    
    DataValueTimestamp() throws InvalidDataTypeException
    {
        super(DataValueType.eTimestamp);
    }
    
    // Takes an integer number of seconds and stores them as a duration.
    
    public DataValueTimestamp(DataValueType type, long value)
        throws InvalidDataTypeException
    {
        super(type);
        
        seconds     = value;
        nanoSeconds = 0;
    }
    
    public DataValueTimestamp(DataValueType type, long value1, long value2)
    throws InvalidDataTypeException
    {
        super(type);
        
        seconds     = value1;
        nanoSeconds = value2;
    }
    
    // Takes a floating point value of seconds and stores them as a duration.
    
    public DataValueTimestamp(DataValueType type, double value)
    throws InvalidDataTypeException
    {
        super(type);
        
        seconds = new Double(value).longValue();
        
        nanoSeconds = new Double((value - seconds) * 1000000000).longValue();
    }
    
    // Takes a string and interprets it as a duration. If it contains x:y format
    // then it is interpreted as seconds:nanoseconds. Otherwise it is passed to the
    // Double() constructor and treated as a possibly fractional number of seconds.
    
    public DataValueTimestamp(DataValueType type, String value)
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
                double realValue = new Double(value).doubleValue();

                seconds = new Double(realValue).longValue();

                nanoSeconds = new Double((realValue - seconds) * 1000000000).longValue();
            }
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException(value + " is not a valid " + type);
        }
    }
    
    private void truncateTimestamp()
    {
        if (truncationInterval != 0)
        {
            // Recalculate the seconds value based on the truncation interval
            // and set the nanoseconds component to zero.
            
            seconds     = (seconds / truncationInterval) * truncationInterval;
            nanoSeconds = 0;
            
            // Ensure that we don't re-calculate by setting the truncation
            // interval to zero.
            
            truncationInterval = 0;
        }
        
        //seconds = (seconds * 1000) + (nanoSeconds)/1000000;
    }
    
    public DataValue newInstance()
    {
        DataValueTimestamp[] other = { null };
        
        try
        {
            other[0] = new DataValueTimestamp(DataValueType.eTimestamp, seconds, nanoSeconds);
            
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
        truncateTimestamp();
        
        return seconds;
    }
    
    public double toReal()
    {
        truncateTimestamp();

        return (double) (seconds + ((double) nanoSeconds / 1000000000));
    }
    
    public boolean toBoolean()
    {
        return (!(seconds == 0 && nanoSeconds == 0));
    }
    
    public String toUnformattedString()
    {
        truncateTimestamp();
        
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
        {
            truncateTimestamp();
            
            return String.format("0x%08x:0x%08x", seconds, nanoSeconds);
        }
        else
        {
            if (formatString.length() == 0)
            {
                return toUnformattedString();
            }
            else
            {
                truncateTimestamp();
		int lastIndex = formatString.lastIndexOf("%");

		String tokens[] = formatString.substring(lastIndex,formatString.length()).split("\\$");
		String lastone = tokens[tokens.length-1];
		if(lastone.startsWith("tLOCAL") || lastone.startsWith("tUTC"))
		{
			//Build the formatString without tailing %1$tLOCAL or %1$tUTC
			String newFmtString = formatString.substring(0,lastIndex);
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis((seconds * 1000) + (nanoSeconds / 1000000));

			TimeZone tz = TimeZone.getTimeZone("UTC");
			if(lastone.startsWith("tLOCAL"))
			{
				String tzSetting = System.getenv("TZ");
				if(tzSetting != null)
				{
					tz = TimeZone.getTimeZone(tzSetting);	
				}
			}
			cal.setTimeZone(tz);
			return String.format(newFmtString, cal);
		}
		else
		{                
                	return String.format(formatString, new Date((seconds * 1000) + (nanoSeconds / 1000000)));
		}
            }
        }
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            truncateTimestamp();
            
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
	ByteBuffer buffer = ByteBuffer.allocate(16);
        UnsignedInts.putUnsignedLong (buffer, seconds);
	UnsignedInts.putUnsignedLong (buffer,nanoSeconds);
        return buffer.array();
    }

	@Override
	public byte[] toFidrBinary() 
	{
	     ByteBuffer buffer= ByteBuffer.allocate(17);
	     UnsignedInts.putUnsignedByte(buffer, 0x05);
	     UnsignedInts.putUnsignedLong(buffer, seconds);
	     UnsignedInts.putUnsignedLong(buffer,nanoSeconds);
	     return buffer.array();
	}
}
