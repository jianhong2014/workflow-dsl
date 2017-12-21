package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jm.wf.common.utils.UnsignedInts;


public class DataValueESN extends DataValueBase
{
    // Pattern to match a, possibly, wildcarded ESN string.
    //
    //                                          Mfr      - Serial
    //                                          Code       Number
    static Pattern esn      = Pattern.compile("([0-9]+)\\-(\\*)");

    protected long    esnValue = 0L;
    protected boolean wildcard = false;
    

    DataValueESN()
    throws InvalidDataTypeException
    {
        super(DataValueType.eESN);
    }

    DataValueESN(DataValueType type, long value)
    throws InvalidDataTypeException
    {
        super(type);
        
        esnValue = value;
        wildcard = false;
    }

    DataValueESN(DataValueType type, String value)
    throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            Matcher esnMatcher = esn.matcher(value);

            if (esnMatcher.matches())
            {
                // Build the value and the mask from the two matching groups. If the second
                // matching group contains '*', the corresponding bits in the value and the
                // mask will be set to '0'.  Otherwise the value bits will be set according
                // to the hex digits and the mask bits will be set to '1'.
                
                String str;
                
                if (esnMatcher.group(1) != null)
                {
                    esnValue = Long.parseLong(esnMatcher.group(1), 10);
                }
                else
                {
                    throw new InvalidDataTypeException();
                }
                
                if (esnMatcher.group(2) != null)
                {
                    str = esnMatcher.group(2);
                    
                    if (str.equals("*"))
                    {
                        wildcard = true;
                    }
                }
                else
                {
                    throw new InvalidDataTypeException();
                }
            }
            else
            {
                esnValue = Long.parseLong(value, 10);
                wildcard = false;
            }
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException(value + " is not a valid " + type);
        }
    }
    
    public void setWildcard(boolean wild)
    {
        wildcard = wild;
    }
    
    public boolean isWildcarded()
    {
        return wildcard;
    }

    public DataValue newInstance()
    {
        DataValueESN[] other = { null };
        
        try
        {
            other[0] = new DataValueESN(DataValueType.eESN, esnValue);
            
            other[0].setWildcard(wildcard);
            
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
        return esnValue;
    }
    
    public double toReal()
    {
        try
        {
            return new Double(esnValue);
        }
        catch (Exception e)
        {
            return 0.0;
        }
    }
    
    public boolean toBoolean()
    {
        return (esnValue != 0);
    }
    
    public String toUnformattedString()
    {
        return new Long(esnValue).toString();
    }
    
    public String toString()
    {
        if (wildcard)
        {
            return String.format("%d-*", esnValue);
        }
        else
        {
            //return String.format("%d", esnValue);
            
            long value = (esnValue & 0xFF000000L) >> 24;
            
            if (value >= 19 && value != 250 && value != 255)
            {
                return String.format("%03d%08d",
                                     (esnValue & 0xFF000000L) >> 24,
                                      esnValue & 0x00FFFFFFL);
            }
            else
            {
                return String.format("%03d%08d",
                                     (esnValue & 0xFFFC0000L) >> 18,
                                      esnValue & 0x0003FFFFL);
            }
        }
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) esnValue;
        }
        return generatedHashCode;
    }
    
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else if (other instanceof DataValueESN && (wildcard || ((DataValueESN) other).isWildcarded()))
        {
            if (wildcard)
            {
                long val = ((DataValueESN) other).toInteger();

                long value = (val & 0xFF000000L) >> 24;
                
                if (value >= 19 && value != 250 && value != 255)
                {
                    return esnValue == ((val & 0xFF000000L) >> 24);
                }
                else
                {
                    return esnValue == ((val & 0xFFFC0000L) >> 18);
                }
            }
            else
            {
                long val = ((DataValueESN) other).toInteger();

                long value = (esnValue & 0xFF000000L) >> 24;
                
                if (value >= 19 && value != 250 && value != 255)
                {
                    return ((esnValue & 0xFF000000L) >> 24) == val;
                }
                else
                {
                    return ((esnValue & 0xFFFC0000L) >> 18) == val;
                }
            }
        }
        else
            return esnValue == ((DataValue) other).toInteger();
    }

    public boolean lessThan(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return false;
        else if (other instanceof DataValueESN && (wildcard || ((DataValueESN) other).isWildcarded()))
        {
            if (wildcard)
            {
                long val = ((DataValueESN) other).toInteger();

                long value = (val & 0xFF000000L) >> 24;
                
                if (value >= 19 && value != 250 && value != 255)
                {
                    return esnValue < ((val & 0xFF000000L) >> 24);
                }
                else
                {
                    return esnValue < ((val & 0xFFFC0000L) >> 18);
                }
            }
            else
            {
                long val = ((DataValueESN) other).toInteger();

                long value = (val & 0xFF000000L) >> 24;
                
                if (esnValue >= 19 && value != 250 && value != 255)
                {
                    return ((esnValue & 0xFF000000L) >> 24) < val;
                }
                else
                {
                    return ((esnValue & 0xFFFC0000L) >> 18) < val;
                }
            }
        }
        else
            return esnValue < ((DataValue) other).toInteger();
    }

    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		// allocate buffer 8 bytes for real and 1 for datatype
		ByteBuffer buffer = ByteBuffer.allocate(5);

		UnsignedInts.putUnsignedByte(buffer, 0x0A);

		UnsignedInts.putUnsignedInt(buffer, esnValue);
		
		return buffer.array();
	}
	
}
