package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jm.wf.common.utils.UnsignedInts;


public class DataValueAdHocService extends DataValueBase
{
    static Pattern ipv4       = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");
    static Pattern ipv4Cidr   = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)/(\\d+)");
    static Pattern ipv4Subnet = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)/(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)");

    static Pattern portNumber = Pattern.compile("(\\d+)|(\\d+)\\-(\\d+)|(\\*)");

    enum IPv4Format
    {
        eIPv4Standard,
        eIPv4CIDR,
        eIPv4Subnet
    }
    
    protected byte[] ipv4Value  = { 0x0, 0x0, 0x0, 0x0 };
    protected byte[] subnetMask = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    protected int    cidrMask   = 32;
    
    private long mask = 0xFFFFFFFF;
    
    private IPv4Format ipv4Format = IPv4Format.eIPv4Standard;
    
    private int portLower = 0;
    private int portUpper = 0xFFFF;
    
    
    DataValueAdHocService() throws InvalidDataTypeException
    {
        super(DataValueType.eAdHocService);
    }

    DataValueAdHocService(DataValueType type, long value)
    throws InvalidDataTypeException
    {
        super(type);

        ipv4Value[0] = (byte) ((value & 0xFF000000) >> 24);
        ipv4Value[1] = (byte) ((value & 0x00FF0000) >> 16);
        ipv4Value[2] = (byte) ((value & 0x0000FF00) >> 8);
        ipv4Value[3] = (byte)  (value & 0x000000FF);
    }

    DataValueAdHocService(DataValueType type, long value1, long value2)
    throws InvalidDataTypeException
    {
        super(type);

        ipv4Value[0] = (byte) ((value1 & 0xFF000000) >> 24);
        ipv4Value[1] = (byte) ((value1 & 0x00FF0000) >> 16);
        ipv4Value[2] = (byte) ((value1 & 0x0000FF00) >> 8);
        ipv4Value[3] = (byte)  (value1 & 0x000000FF);
        
        portLower = (int) ((value2 & 0xFFFF0000L) >> 16);
        portUpper = (int)  (value2 & 0x0000FFFFL);
    }

    DataValueAdHocService(DataValueType type, String value)
    throws InvalidDataTypeException
    {
        super(type);
        
        String[] ahService = value.split(":");
        
        if (ahService.length == 2)
        {
            // Start by considering the IP Address part...
            
            Matcher ipv4Matcher       = ipv4.matcher(ahService[0]);
            Matcher ipv4CidrMatcher   = ipv4Cidr.matcher(ahService[0]);
            Matcher ipv4SubnetMatcher = ipv4Subnet.matcher(ahService[0]);
            
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
                            throw new InvalidDataTypeException("IPv4 Address Part out of range: " + ahService[0]);
                        }
                    }
                    else
                    {
                        throw new InvalidDataTypeException("Malformed IPv4 Address: " + ahService[0]);
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
                            throw new InvalidDataTypeException("IPv4 Address Part out of range: " + ahService[0]);
                        }
                    }
                    else
                    {
                        throw new InvalidDataTypeException("Malformed IPv4 Address: " + ahService[0]);
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
                        throw new InvalidDataTypeException("IPv4 Address Part out of range: " + ahService[0]);
                    }
                }
                else
                {
                    throw new InvalidDataTypeException("Malformed IPv4 Address: " + ahService[0]);
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
                            throw new InvalidDataTypeException("IPv4 Address Part out of range: " + ahService[0]);
                        }
                    }
                    else
                    {
                        throw new InvalidDataTypeException("Malformed IPv4 Address: " + ahService[0]);
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
                            throw new InvalidDataTypeException("IPv4 Address Part out of range: " + ahService[0]);
                        }
                    }
                    else
                    {
                        throw new InvalidDataTypeException("Malformed IPv4 Address: " + ahService[0]);
                    }
                }

                setSubnetMask(subnet);
            }
            else
            {
                throw new InvalidDataTypeException("Malformed IPv4 Address: " + ahService[0]);
            }

            
            // Now look at the port number expression...
            
            Matcher portNumberMatcher = portNumber.matcher(ahService[1]);

            if (portNumberMatcher.matches())
            {
                if (portNumberMatcher.group(1) != null)
                {
                    // Expression matched a single port number: port
                    
                    portLower = new Integer(portNumberMatcher.group(1));
                    portUpper = portLower;
                }
                else if (portNumberMatcher.group(2) != null && portNumberMatcher.group(3) != null)
                {
                    // Expression matched a range: lower-upper
                    
                    portLower = new Integer(portNumberMatcher.group(2));
                    portUpper = new Integer(portNumberMatcher.group(3));
                }
                else if (portNumberMatcher.group(4) != null)
                {
                    // Expression matched a wildcarded port range: *
                    
                    portLower = 0;
                    portUpper = 0xFFFF;
                }
                else
                {
                    throw new InvalidDataTypeException("Malformed Port Number: " + ahService[1]);
                }
                
                if (portLower < 0 || portUpper > 0xFFFF || portLower > portUpper)
                {
                    throw new InvalidDataTypeException("Malformed Port Number: " + ahService[1]);
                }
            }
            else
            {
                throw new InvalidDataTypeException("Malformed Port Number: " + ahService[1]);
            }
        }
        else
        {
            throw new InvalidDataTypeException("Malformed Ad Hoc Service: " + value);
        }
    }

    public synchronized byte[] getIpv4Value()
    {
        return ipv4Value;
    }

    public synchronized void setIpv4Value(byte[] ipv4Value)
    {
        this.ipv4Value = ipv4Value;
    }

    public synchronized void setSubnetMask(byte[] subnetMask)
    {
        if (subnetMask.length >= 4)
        {
            this.subnetMask = subnetMask;
        
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

    public synchronized long getMask()
    {
        return mask;
    }

    public synchronized int getPortLower()
    {
        return portLower;
    }

    public synchronized void setPortLower(int portLower)
    {
        this.portLower = portLower;
    }

    public synchronized int getPortUpper()
    {
        return portUpper;
    }

    public synchronized void setPortUpper(int portUpper)
    {
        this.portUpper = portUpper;
    }

    public DataValue newInstance()
    {
        DataValueAdHocService[] other = { null };
        
        try
        {
            other[0] = new DataValueAdHocService();
            
            other[0].setIpv4Value(ipv4Value);
            
            if (ipv4Format == IPv4Format.eIPv4CIDR)
            {
                other[0].setCidrMask(cidrMask);
            }
            else if (ipv4Format == IPv4Format.eIPv4Subnet)
            {
                other[0].setSubnetMask(subnetMask);
            }
            
            other[0].setPortLower(portLower);
            other[0].setPortUpper(portUpper);
            
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
        return (((long) ipv4Value[0] & 0x000000FF) << 56) +
               (((long) ipv4Value[1] & 0x000000FF) << 48) +
               (((long) ipv4Value[2] & 0x000000FF) << 40) +
               (((long) ipv4Value[3] & 0x000000FF) << 32) +
               (((long) portLower & 0x0000FFFF) << 16) +
                ((long) portUpper & 0x0000FFFF);
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
        
        if (portLower == portUpper)
        {
            result += ":" + portLower;
        }
        else
        {
            result += ":" + portLower + "-" + portUpper;
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
            generatedHashCode = (int) toInteger();
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
        else if (other instanceof DataValueAdHocService)
        {
            long combinedMask = (mask & ((DataValueAdHocService) other).getMask()) << 32;
            
            int otherLower = ((DataValueAdHocService) other).getPortLower();
            int otherUpper = ((DataValueAdHocService) other).getPortUpper();
            
/*            System.out.println("combinedMask: " + String.format("%016X", combinedMask));
            System.out.println("this: " + String.format("%016X", toInteger()) + "  other: " + String.format("%016X", ((DataValueAdHocService) other).toInteger()));
            System.out.println("thisMasked: " + String.format("%016X", (toInteger() & combinedMask)) + "  otherMasked: " + String.format("%016X", (((DataValueAdHocService) other).toInteger() & combinedMask)));
            System.out.println("otherLower: " + (portLower <= otherLower && otherLower <= portUpper));
            System.out.println("otherUpper: " + (portLower <= otherUpper && otherUpper <= portUpper));
            System.out.println("portLower:  " + (otherLower <= portLower && portLower <= otherUpper));
            System.out.println("portUpper:  " + (otherLower <= portUpper && portUpper <= otherUpper));
*/            
            return (toInteger() & combinedMask) == (((DataValueAdHocService) other).toInteger() & combinedMask) &&
                   ((portLower <= otherLower && otherLower <= portUpper) ||
                    (portLower <= otherUpper && otherUpper <= portUpper) ||
                    (otherLower <= portLower && portLower <= otherUpper) ||
                    (otherLower <= portUpper && portUpper <= otherUpper));
        }
        else
            return toInteger() == ((DataValue) other).toInteger();
    }

    public boolean lessThan(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (other == this)
        {
            return true;
        }
        else if (other instanceof DataValueAdHocService)
        {
            long combinedMask = (mask & ((DataValueAdHocService) other).getMask()) << 32;
            
            return ((toInteger() & combinedMask) < (((DataValueAdHocService) other).toInteger() & combinedMask)) &&
                   (portUpper < ((DataValueAdHocService) other).getPortLower());
        }
        else
        {
            return toInteger() < ((DataValue) other).toInteger();
        }
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		int[] ipv4Digits = new int[4];
		ipv4Digits[0] = (int) (ipv4Value[0] & 0x000000FF);
		ipv4Digits[1] = (int) (ipv4Value[1] & 0x000000FF);
		ipv4Digits[2] = (int) (ipv4Value[2] & 0x000000FF);
		ipv4Digits[3] = (int) (ipv4Value[3] & 0x000000FF);

		ByteBuffer buffer = ByteBuffer.allocate(7);
		UnsignedInts.putUnsignedByte(buffer, 0x0B);
		for (int i = 0; i < ipv4Digits.length; i++) 
		{
			UnsignedInts.putUnsignedByte(buffer, ipv4Digits[i]);
		}
		UnsignedInts.putUnsignedShort(buffer, portLower);
		return buffer.array();
	}
}
