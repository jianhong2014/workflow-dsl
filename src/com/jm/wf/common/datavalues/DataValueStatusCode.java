package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueStatusCode extends DataValueBase 
{
	private static final long MULTIPLIER = 0x100000000L;
	
	protected long protocol    = 0L;
	protected long code        = 0L;
	protected boolean wildcard = false;
		    
	DataValueStatusCode()
	throws InvalidDataTypeException
    {
        super(DataValueType.eStatusCode);
    }
    
	DataValueStatusCode(DataValueType type, long protocol, long code)
	throws InvalidDataTypeException
    {
        super(type);
        
        this.protocol = protocol;
        this.code = code;
        
        if (!isProtocolSupported(protocol))
        {
            throw new InvalidDataTypeException("Unsupported protocol: " + protocol);
        }
    }

	DataValueStatusCode(DataValueType type, String value)
	throws InvalidDataTypeException
    {
        super(type);
    
        /*
         * Supported formats are 103:330, HTTP:330 or HTTP:*
         */                
        try
        {
        	String[] split = value.split(":");
        	        	
        	if (split.length == 2)
        	{
        	    // Check if protocol is a name instead of a number
        	    try
        	    {
        	        protocol = Long.decode(split[0]);
        	        
        	        // Check we support this protocol code
        	        if (!isProtocolSupported(protocol))
                    {
                        throw new InvalidDataTypeException("Unsupported protocol: " + protocol);
                    }
        	    }
        	    catch (Exception e)
        	    {
        	        try
        	        {
        	            // Lookup the name supplied to get the protocol ID
        	            protocol = getIdFromName(split[0]);
        	        }
        	        catch (Exception e1)
        	        {
        	            throw new InvalidDataTypeException("Unsupported protocol: " + split[0]);
        	        }
        	    }                
        	     
        	    try
        	    {
        	        if (split[1].equals("*"))
        	        {
        	            // Wild card allows all code values to match for a given protocol
        	            wildcard = true;
        	        }
        	        else
        	        {
        	            code = Long.decode(split[1]);
        	        }
        	    }
        	    catch (Exception e)
        	    {
        	        throw new InvalidDataTypeException("Unsupported protocol code: " + split[1]);
        	    }
        	}
        	else
        	{
        		throw new InvalidDataTypeException(value + " is not a valid " + type);
        	}
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException(value + " is not a valid " + type);
        }
    }
	
	private boolean isProtocolSupported (long protocolCode)
	{
	    boolean supported = false;   
	    
	    return (supported);	    
	}
	
	private long getIdFromName (String protocolName) throws InvalidDataTypeException
	{
	    long id = -1L;
	    
	    if (id == -1)
	    {
	        new InvalidDataTypeException(protocolName); 
	    }
	    
	    return id;
	}
    
    public DataValue newInstance()
    {
    	DataValueStatusCode[] other = { null };
    	
    	try
        {
            other[0] = new DataValueStatusCode(DataValueType.eStatusCode, protocol, code);
            
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
        return (protocol * MULTIPLIER) + code;
    }
    
    public boolean toBoolean()
    {
        return (toInteger() != 0);
    }
    
    public String toUnformattedString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(protocol).append(":").append(code);
        
        return sb.toString();
    }
    
    public String toString()
    {
        return toUnformattedString();
    }
    
    public int hashCode()
    {
    	if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = (int) (protocol * code);
        }
    	
        return generatedHashCode;
    }
    
    public void setWildcard(boolean wild)
    {
        wildcard = wild;
    }
    
    private long getProtocolValue (long protocolStatusCode)
    {
        return ((protocolStatusCode & 0xFFFF0000) >> 32); 
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
        else if (wildcard)
        {
            return getProtocolValue(toInteger()) == getProtocolValue(((DataValue) other).toInteger());
        }
        else
        {
            return toInteger() == ((DataValue) other).toInteger();
        }
    }
    
    public boolean lessThan(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (other == this)
        {
            return false;
        }
        else if (wildcard)
        {
            return getProtocolValue(toInteger()) < getProtocolValue(((DataValue) other).toInteger()); 
        }
        else
        {        
            return (toInteger() < ((DataValue) other).toInteger());
        }
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

		UnsignedInts.putUnsignedByte(buffer, 0x0C);

		UnsignedInts.putUnsignedInt(buffer, protocol);
		
		UnsignedInts.putUnsignedInt(buffer, code);
		
		return buffer.array();
	}
}
