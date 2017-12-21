package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueIPv4Address extends DataValueBase
{
    static Pattern ipv4       = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");
    static Pattern ipv4Cidr   = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)/(\\d+)");
    static Pattern ipv4Subnet = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)/(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");

    enum IPv4Format
    {
        eIPv4Standard,
        eIPv4CIDR,
        eIPv4Subnet
    }
    
    protected byte[] ipv4Value  = { 0x0, 0x0, 0x0, 0x0 };
    protected byte[] subnetMask = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    protected int    cidrMask   = 32;
    
    private int mask = 0xFFFFFFFF;
    
    private IPv4Format ipv4Format = IPv4Format.eIPv4Standard;
    
    
    public DataValueIPv4Address() throws InvalidDataTypeException
    {
        super(DataValueType.eIPv4Address);
    }
    
    public DataValueIPv4Address(DataValueType type, long value)
        throws InvalidDataTypeException
    {
        super(type);

        ipv4Value[0] = (byte) ((value & 0xFF000000) >> 24);
        ipv4Value[1] = (byte) ((value & 0x00FF0000) >> 16);
        ipv4Value[2] = (byte) ((value & 0x0000FF00) >> 8);
        ipv4Value[3] = (byte)  (value & 0x000000FF);
    }
    
    public DataValueIPv4Address(DataValueType type, String value)
        throws InvalidDataTypeException
    {
        super(type);
        
        Matcher ipv4Matcher       = ipv4.matcher(value);
        Matcher ipv4CidrMatcher   = ipv4Cidr.matcher(value);
        Matcher ipv4SubnetMatcher = ipv4Subnet.matcher(value);
        
        if (ipv4Matcher.matches())
        {
            for (int i = 0; i < 4; i++)
            {
                if (ipv4Matcher.group(i) != null)
                {
                    int addrPart = new Integer(ipv4Matcher.group(i+1));

                    if (addrPart >= 0 && addrPart <= 255)
                    {
                        ipv4Value[i] = (byte) addrPart;
                    }
                    else
                    {
                        throw new InvalidDataTypeException("IPv4 Address Part out of range: " + value);
                    }
                }
                else
                {
                    throw new InvalidDataTypeException("Malformed IPv4 Address: " + value);
                }
            }
        }
        else if (ipv4CidrMatcher.matches())
        {
            for (int i = 0; i < 4; i++)
            {
                String group = ipv4CidrMatcher.group(i+1);
                
                if (group != null)
                {
                    int addrPart = new Integer(group);

                    if (addrPart >= 0 && addrPart <= 255)
                    {
                        ipv4Value[i] = (byte) addrPart;
                    }
                    else
                    {
                        throw new InvalidDataTypeException("IPv4 Address Part out of range: " + value);
                    }
                }
                else
                {
                    throw new InvalidDataTypeException("Malformed IPv4 Address: " + value);
                }
            }
            
            String group = ipv4CidrMatcher.group(5);
            
            if (group != null)
            {
                int cidrPart = new Integer(group);

                if (cidrPart >= 0 && cidrPart <= 32)
                {
                    cidrMask = cidrPart;
                    
                    setCidrMask(cidrMask);
                }
                else
                {
                    throw new InvalidDataTypeException("IPv4 Address Part out of range: " + value);
                }
            }
            else
            {
                throw new InvalidDataTypeException("Malformed IPv4 Address: " + value);
            }
        }
        else if (ipv4SubnetMatcher.matches())
        {
            for (int i = 0; i < 4; i++)
            {
                String group = ipv4SubnetMatcher.group(i+1);
                
                if (group != null)
                {
                    int addrPart = new Integer(group);

                    if (addrPart >= 0 && addrPart <= 255)
                    {
                        ipv4Value[i] = (byte) (addrPart & 0x000000FF);
                    }
                    else
                    {
                        throw new InvalidDataTypeException("IPv4 Address Part out of range: " + value);
                    }
                }
                else
                {
                    throw new InvalidDataTypeException("Malformed IPv4 Address: " + value);
                }
            }

            byte[] subnet = new byte[4];
            
            for (int i = 4; i < 8; i++)
            {
                String group = ipv4SubnetMatcher.group(i+1);
                
                if (group != null)
                {
                    int maskPart = new Integer(group);

                    if (maskPart >= 0 && maskPart <= 255)
                    {
                        subnet[i-4] = (byte) (maskPart & 0x000000FF);
                    }
                    else
                    {
                        throw new InvalidDataTypeException("IPv4 Address Part out of range: " + value);
                    }
                }
                else
                {
                    throw new InvalidDataTypeException("Malformed IPv4 Address: " + value);
                }
            }
            
            setSubnetMask(subnet);
        }
        else
        {
            throw new InvalidDataTypeException("Malformed IPv4 Address: " + value);
        }
    }
    
    public DataValueIPv4Address(DataValueType type, byte [] data)
        throws InvalidDataTypeException
    {
        super(type);
        
        if (data.length >= 4)
        {
            //for (int i = 0; i < 4; i++)
            {
                ipv4Value/*[i]*/ = data/*[i]*/;
            }
        }
        else
            throw new InvalidDataTypeException("IPv4 Address must be 4 octets");
    }
    
    public synchronized void setSubnetMask(byte[] subnetMask)
    {
        if (subnetMask.length >= 4)
        {
            //for (int i = 0; i < 4; i++)
            {
                this.subnetMask/*[i]*/ = subnetMask/*[i]*/;
            }
        
            mask = (((int) subnetMask[0] & 0x000000FF) << 24) +
                   (((int) subnetMask[1] & 0x000000FF) << 16) +
                   (((int) subnetMask[2] & 0x000000FF) << 8) +
                    ((int) subnetMask[3] & 0x000000FF);
            
            ipv4Format = IPv4Format.eIPv4Subnet;
        }
    }

    public synchronized void setCidrMask(int sigMSBs)
    {
        mask = ((int) (Math.pow(2, sigMSBs) - 1)) << (32 - sigMSBs);
        
        ipv4Format = IPv4Format.eIPv4CIDR;
    }

    public synchronized int getMask()
    {
        return mask;
    }

    public DataValue newInstance()
    {
        DataValueIPv4Address[] other = { null };
        
        try
        {
            other[0] = new DataValueIPv4Address(DataValueType.eIPv4Address, ipv4Value);
            
            if (ipv4Format == IPv4Format.eIPv4CIDR)
            {
                other[0].setCidrMask(cidrMask);
            }
            else if (ipv4Format == IPv4Format.eIPv4Subnet)
            {
                other[0].setSubnetMask(subnetMask);
            }
            
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
        return ((((int) ipv4Value[0] & 0x000000FF) << 24) +
                (((int) ipv4Value[1] & 0x000000FF) << 16) +
                (((int) ipv4Value[2] & 0x000000FF) << 8) +
                 ((int) ipv4Value[3] & 0x000000FF)) & 0x00000000FFFFFFFF;
    }
    
    public boolean toBoolean()
    {
        return (toInteger() != 0);
    }

    public String toUnformattedString()
    {
        String result = "";
        
        switch (ipv4Format)
        {
            case eIPv4Standard:
                result = String.format("%03d.%03d.%03d.%03d",
                                       (int) (ipv4Value[0] & 0x000000FF),
                                       (int) (ipv4Value[1] & 0x000000FF),
                                       (int) (ipv4Value[2] & 0x000000FF),
                                       (int) (ipv4Value[3] & 0x000000FF));
                break;
            
            case eIPv4CIDR:
                result = String.format("%03d.%03d.%03d.%03d/%d",
                                       (int) (ipv4Value[0] & 0x000000FF),
                                       (int) (ipv4Value[1] & 0x000000FF),
                                       (int) (ipv4Value[2] & 0x000000FF),
                                       (int) (ipv4Value[3] & 0x000000FF),
                                       cidrMask);
                break;

            case eIPv4Subnet:
                result = String.format("%03d.%03d.%03d.%03d/%03d.%03d.%03d.%03d",
                                       (int) (ipv4Value[0] & 0x000000FF),
                                       (int) (ipv4Value[1] & 0x000000FF),
                                       (int) (ipv4Value[2] & 0x000000FF),
                                       (int) (ipv4Value[3] & 0x000000FF),
                                       (int) (subnetMask[0] & 0x000000FF),
                                       (int) (subnetMask[1] & 0x000000FF),
                                       (int) (subnetMask[2] & 0x000000FF),
                                       (int) (subnetMask[3] & 0x000000FF));
                break;
        }

        return result;
    }
    
    public String toString()
    {
        return toUnformattedString();
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) (toInteger() & mask);
        }
        
        return generatedHashCode;
    }
    
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (other == this)
        {
            return true;
        }
        else if (other instanceof DataValueIPv4Address)
        {
            int combinedMask = mask & ((DataValueIPv4Address) other).getMask();
            
            return ((int) toInteger() & combinedMask) ==
                   (((DataValueIPv4Address) other).toInteger() & combinedMask);
        }
        else
            return toInteger() == ((DataValue) other).toInteger();
    }
    
    public boolean lessThan(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return false;
        else if (other instanceof DataValueIPv4Address)
        {
            int combinedMask = mask & ((DataValueIPv4Address) other).getMask();
            
            return ((int) toInteger() & combinedMask) <
                   (((DataValueIPv4Address) other).toInteger() & combinedMask);
        }
        else
            return toInteger() < ((DataValue) other).toInteger();
    }
    
    public byte [] asByteArray()
    {
        return ipv4Value;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		int[] ipv4Digits = new int[4];
		ipv4Digits[0] = (int) (ipv4Value[0] & 0x000000FF);
		ipv4Digits[1] = (int) (ipv4Value[1] & 0x000000FF);
		ipv4Digits[2] = (int) (ipv4Value[2] & 0x000000FF);
		ipv4Digits[3] = (int) (ipv4Value[3] & 0x000000FF);

		ByteBuffer buffer = ByteBuffer.allocate(5);
		UnsignedInts.putUnsignedByte(buffer, 0x07);
		for (int i = 0; i < ipv4Digits.length; i++) 
		{
			UnsignedInts.putUnsignedByte(buffer, ipv4Digits[i]);
		}
		return buffer.array();
	}
    
}
