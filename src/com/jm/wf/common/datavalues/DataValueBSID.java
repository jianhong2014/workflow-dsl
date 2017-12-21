package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueBSID extends DataValueBase
{
    // Pattern to match a, possibly, wildcarded BSID string.
    //
    //                            SID(System Identification) -NID(Network Identification) -CellID/Sector
    static Pattern bsid      = Pattern.compile("([0-9]+|\\*)\\-([0-9]+|\\*)\\-([0-9\\-]+|\\*)");

    protected long bsidValue = 0L;
    protected long bsidMask  = 0L;
    

    public DataValueBSID()
    throws InvalidDataTypeException
    {
        super(DataValueType.eBSID);
    }

    public DataValueBSID(DataValueType type, long value)
    throws InvalidDataTypeException
    {
        super(type);
        
        bsidValue = value;
        bsidMask  = 0x0000FFFFFFFFFFFFL;
    }

    public DataValueBSID(DataValueType type, String value)
    throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            String [] bsidFormats = value.split(" ");
            
            Matcher bsidMatcher = bsid.matcher(bsidFormats[0]);

            if (bsidMatcher.matches())
            {
                // Build the value and the mask from the three matching groups. Where either
                // the second or third matching group contains '*', the corresponding bits
                // in the value and the mask will be set to '0000'.  Otherwise the value bits
                // will be set according to the hex digits and the mask bits will be set to
                // 'FFFF'.
                
                String str;
                
                if (bsidMatcher.group(1) != null)
                {
                    str = bsidMatcher.group(1);
                    
                    if (str.equals("*"))
                    {
                        bsidMask = 0x00000000FFFFFFFFL;
                    }
                    else
                    {
                        bsidValue = (Long.parseLong(str, 10) << 32);
                        bsidMask  = 0x0000FFFF00000000L;
                    }
                }
                else
                {
                    throw new InvalidDataTypeException();
                }
                
                if (bsidMatcher.group(2) != null)
                {
                    str = bsidMatcher.group(2);
                    
                    if (str.equals("*"))
                    {
                        bsidMask &= 0x0000FFFF0000FFFFL;
                    }
                    else
                    {
                        bsidValue |= (Long.parseLong(str, 10) << 16);
                        bsidMask  |= 0x00000000FFFF0000L;
                    }
                }
                else
                {
                    throw new InvalidDataTypeException();
                }
                
                if (bsidMatcher.group(3) != null)
                {
                    str = bsidMatcher.group(3);
                    
                    if (str.equals("*"))
                    {
                        bsidMask &= 0x0000FFFFFFFF0000L;
                    }
                    else
                    {
                        if (str.contains("-"))
                        {
                            String[] parts = str.split("-");
                            
                            long part1 = Long.parseLong(parts[0], 10);
                            long part2 = Long.parseLong(parts[1], 10);

                            bsidValue |= ((part1 * 16) + part2);
                        }
                        else
                        {
                            bsidValue |= Long.parseLong(str, 10);
                        }
                        
                        bsidMask  |= 0x000000000000FFFFL;
                    }
                }
                else
                {
                    throw new InvalidDataTypeException();
                }
            }
            else
            {
                bsidValue = Long.parseLong(value, 16);
                bsidMask  = 0x0000FFFFFFFFFFFFL;
            }
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException(value + " is not a valid " + type);
        }
    }
    
    public void setMask(long mask)
    {
        bsidMask = mask;
    }
    
    public long getMask()
    {
        return bsidMask;
    }

    public DataValue newInstance()
    {
        DataValueBSID[] other = { null };
        
        try
        {
            other[0] = new DataValueBSID(DataValueType.eBSID, bsidValue);
            
            other[0].setMask(bsidMask);
            
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
        return bsidValue;
    }
    
    public double toReal()
    {
        try
        {
            return new Double(bsidValue);
        }
        catch (Exception e)
        {
            return 0.0;
        }
    }
    
    public boolean toBoolean()
    {
        return (bsidValue != 0);
    }
    
    public String toUnformattedString()
    {
        return String.format("%012X", bsidValue);
    }
    
    public String toString()
    {
       /* return String.format("%d-%d-%d %d-%d-%d-%d",
                             ((bsidValue & 0x0000FFFF00000000L) >> 32),
                             ((bsidValue & 0x00000000FFFF0000L) >> 16),
                              (bsidValue & 0x000000000000FFFFL),
                             ((bsidValue & 0x0000FFFF00000000L) >> 32),
                             ((bsidValue & 0x00000000FFFF0000L) >> 16),
                             ((bsidValue & 0x000000000000FFF0L) >> 4),
                              (bsidValue & 0x000000000000000FL));*/
        return String.format("%d-%d-%d",
                ((bsidValue & 0x0000FFFF00000000L) >> 32),
                ((bsidValue & 0x00000000FFFF0000L) >> 16),
                 (bsidValue & 0x000000000000FFFFL));
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) (bsidValue & bsidMask);
        }
        return generatedHashCode;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else if (other instanceof DataValueBSID)
            return (bsidValue & ((DataValueBSID) other).getMask()) == (((DataValueBSID) other).toInteger() & bsidMask);
        else
            return bsidValue == (((DataValue) other).toInteger() & bsidMask);
    }

    @Override
    public boolean lessThan(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return false;
        else if (other instanceof DataValueBSID)
            return (bsidValue & ((DataValueBSID) other).getMask()) < (((DataValueBSID) other).toInteger() & bsidMask);
        else
            return bsidValue < (((DataValue) other).toInteger() & bsidMask);
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		// allocate buffer 8 bytes for real and 1 for datatype
		ByteBuffer buffer = ByteBuffer.allocate(9);

		UnsignedInts.putUnsignedByte(buffer, 0x09);

		UnsignedInts.putUnsignedLong(buffer, bsidValue);

		return buffer.array();
	}

}
