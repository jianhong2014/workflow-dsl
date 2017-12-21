package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.common.utils.UnsignedInts;


public class DataValueString extends DataValueBase
{
    protected String  strValue = "";
    protected Pattern strRegex = null;
    
    public DataValueString() throws InvalidDataTypeException
    {
        super(DataValueType.eString);
    }
    
    public DataValueString(DataValueType type,
                           String        value) throws InvalidDataTypeException
    {
        super(type);
        
        strValue = value;
        
        if (strValue.contains("*"))
        {
        	try
        	{
        		strRegex = Pattern.compile(strValue.replace(".", "\\.").replace("*", ".*"), Pattern.CASE_INSENSITIVE);
        	}
        	catch(PatternSyntaxException pse)
        	{
        		LoggerManager.getLogger().info("PatternSyntaxException while creating regEx for string " + strValue);
        		LoggerManager.getLogger().info("No Regular expression created. Exception : " + pse.getMessage());
        	}
        }
    }
    
    public DataValueString(DataValueType type,
                           long          value) throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            strValue = new Long(value).toString();
        }
        catch (Exception e)
        {
            strValue = "";
        }
    }
    
    public DataValueString(DataValueType type,
                           double        value) throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            strValue = new Double(value).toString();
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException("Invalid double precision float");
        }
    }
    
    public DataValueString(DataValueType type,
                           boolean       value) throws InvalidDataTypeException
    {
        super(type);
        
        try
        {
            strValue = new Boolean(value).toString();
        }
        catch (Exception e)
        {
            throw new InvalidDataTypeException("Invalid boolean representation");
        }
    }
    
    public Pattern getStrRegex()
    {
        return strRegex;
    }

    public DataValue newInstance()
    {
        DataValueString[] other = { null };
        
        try
        {
            other[0] = new DataValueString(DataValueType.eString, strValue);
            
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
        try
        {
            return Long.decode(strValue);
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    public double toReal()
    {
        try
        {
            return new Double(strValue);
        }
        catch (Exception e)
        {
            return 0.0;
        }
    }
    
    public boolean toBoolean()
    {
        return (!(strValue.length() == 0 || strValue.equals("0") || strValue.equalsIgnoreCase("false")));
    }
    
    public String toUnformattedString()
    {
        return strValue;
    }
    
    public String toString()
    {
        if (formatString.length() == 0)
        {
            return toUnformattedString();
        }
        else
        {
            return String.format(formatString, strValue);
        }
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = strValue.toLowerCase().hashCode();
        }
        
        return generatedHashCode;
    }
    
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else if (strRegex != null)
        {
            return strRegex.matcher(((DataValue) other).toUnformattedString()).matches();
        }
        else if ((other instanceof DataValueString) && (((DataValueString) other).getStrRegex() != null))
        {
            return ((DataValueString) other).getStrRegex().matcher(toUnformattedString()).matches();
        }
        else
            return strValue.equalsIgnoreCase(((DataValue) other).toUnformattedString());
    }
    
    public boolean lessThan(Object other)
    {
        return strValue.compareToIgnoreCase(((DataValue) other).toUnformattedString()) < 0;
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

    
	@Override
	public int length() 
	{
		return strValue.length();
	}

	@Override
	public byte[] toFidrBinary() 
	{
   	     int len = strValue.length();
		 int offset=0;
		 
		//allocate buffer for length of string and
		//2bytes for length 1 byte for data type
		 ByteBuffer buffer=ByteBuffer.allocate(len+3); 
		
	     UnsignedInts.putUnsignedByte(buffer, 0x04);
	     offset=offset+1;
	     
	     UnsignedInts.putUnsignedShort(buffer, len);
	     offset=offset+2;
	                                  
	     System.arraycopy(strValue.getBytes(), 0, buffer.array(), offset, len);
		
	     return buffer.array();
	}
}
