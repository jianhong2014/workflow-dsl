package com.jm.wf.common.datavalues;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.common.utils.UnsignedInts;

public class DataValuePointCode extends DataValueBase
{
//    static Pattern itu        = Pattern.compile("(\\d)\\-(\\d+)\\-(\\d)");
//    static Pattern ansi       = Pattern.compile("(\\d+)\\-(\\d+)\\-(\\d+)");
//    static Pattern decimal    = Pattern.compile("(\\d+)");

    static Pattern itu        = Pattern.compile("([0-9&&[^89]])\\-([0-9]+)\\-([0-9&&[^89]])");
    static Pattern ansi       = Pattern.compile("(\\d+)\\-(\\d+)\\-(\\d+)");
    static Pattern decimal    = Pattern.compile("(\\d+)");
    
    private String ituString  = "ITU";
    private String ansiString = "ANSI";
    
    enum PCFormat
    {
        ePCITU,
        ePCANSI,
        ePCDecimal
    }
    
    
    /**
     * @param rawPC : ubit32 from feed
     * @return long : 24 or 14 bit point code
     * 
     *  Extracts the point code and sets the type of point code
     */
    private long getPC(long rawPC)
    {
    	long ni = getNI(rawPC);
    	long formattedPC = 0;
    	
    	if (ni == 2)
    	{
    		// National network, ANSI888, 24 bits
    		pcFormat = PCFormat.ePCANSI;
    		formattedPC = (rawPC & 0x00ffffff);
    	}
    	else if (ni == 0)
    	{
    		// International network, ITU383 14 bits
    		pcFormat = PCFormat.ePCITU;
    		formattedPC = (rawPC & 0x00003fff);
    	}
    	else
    	{
    		// We have not been able to identify the point code format
    		// we will strip off the top 8 bits and treat it as decimal
    		pcFormat = PCFormat.ePCDecimal;
    		formattedPC = (rawPC & 0x00ffffff);
    	}
    	
    	return formattedPC;
    }
    
    private long getNI(long rawPC)
    {
    	return ((rawPC & (0x03 << 26)) >> 26); 
    }
    
    private long getPC383(int a, int b, int c)
    {
    	return ( (long) ((( a & 0x00FF) << 11) + (b << 3) + (c & 0x00FF) ));
    }
    
    private long getPC888(int a, int b, int c)
    {
    	return ( (long) ( (a << 16) + (b << 8) + (c)));
    }
    
    protected int[] pcValue    = { 0x0, 0x0, 0x0 };
    protected long longValue;
    
    private PCFormat pcFormat = PCFormat.ePCDecimal;
    
    public DataValuePointCode() throws InvalidDataTypeException
    {
        super(DataValueType.ePointCode);
    }
    
    public DataValuePointCode(DataValueType type, long value)
        throws InvalidDataTypeException
    {
        super(type);
        
        // The top 8 bits are flag fields, need to extract the lower 24 bits
        
        longValue = getPC(value);
        
        // getPC sets the point code format, so we dont have to do it again
        if (pcFormat == PCFormat.ePCANSI)
        {
        	pcValue[0] = (int) ((longValue >> 16) & 0xFF);
        	pcValue[1] = (int) ((longValue >> 8) & 0xFF);
        	pcValue[2] = (int) (longValue & 0xFF);
        }
        else if (pcFormat == PCFormat.ePCITU)
        {
        	pcValue[0] = (int) ((longValue >> 11) & 0x07);
        	pcValue[1] = (int) ((longValue >> 3) & 0xFF);
        	pcValue[2] = (int) (longValue & 0x07);
        }
    }
    
    public DataValuePointCode(DataValueType type, String value)
        throws InvalidDataTypeException
    {
        super(type);
        
        Matcher decimalMatcher  = decimal.matcher(value);
        
        
        if (decimalMatcher.matches())
        {
        	longValue = Integer.parseInt(value);
        	pcFormat  = PCFormat.ePCDecimal;
        }
        else
        {
        	String [] splitString = null;
        	splitString = value.split("\\:");
        	
        	Matcher ituMatcher     = itu.matcher(splitString[1]);
            Matcher ansiMatcher    = ansi.matcher(splitString[1]);
        	
        	if (splitString != null)
        	{
        		if (splitString[0].equalsIgnoreCase(ituString))
        		{
        			if (ituMatcher.matches())
        			{
        				for (int i = 0; i < 3; i++)
        				{
        					if (ituMatcher.group(i) != null)
        					{
        						int addrPart = new Integer(ituMatcher.group(i+1));

        						switch (i)
        						{
        						case 0:
        						case 2:
        						{
        							if (addrPart >= 0 && addrPart <= 7)
        							{
        								pcValue[i] = (byte) addrPart;
        							}
        							else
        							{
        								throw new InvalidDataTypeException("PointCode part out of range: " + value);
        							}
        						}
        						break;

        						case 1:
        						{
        							if (addrPart >= 0 && addrPart <= 255 )
        							{
        								pcValue[i] = addrPart;
        							}
        							else
        							{
        								throw new InvalidDataTypeException("PointCode part out of range: " + value);
        							}
        						}
        						break;
        						}
        					}
        					else
        					{
        						throw new InvalidDataTypeException("Malformed PointCode: " + value);
        					}
        				}
        			}

        			pcFormat = PCFormat.ePCITU;
        		}
        		else if (splitString[0].equalsIgnoreCase(ansiString))
        		{
                    if (LoggerManager.getLogger().isDebugEnabled())
                    {
                        LoggerManager.getLogger().debug("Ansi pointcode");
                    }
        			
        			if (ansiMatcher.matches())
        			{
        				for (int i = 0; i < 3; i++)
        				{
        					String group = ansiMatcher.group(i+1);

        					if (group != null)
        					{
        						int addrPart = new Integer(group);

        						if (addrPart >= 0 && addrPart <= 255)
        						{
        							pcValue[i] = addrPart;
        						}
        						else
        						{
        							throw new InvalidDataTypeException("PointCode part out of range: " + value);
        						}
        					}
        					else
        					{
        						throw new InvalidDataTypeException("Malformed PointCode: " + value);
        					}
        				}
        			}

        			pcFormat = PCFormat.ePCANSI;
        		}
        	}
        }
    }
    
    public DataValue newInstance()
    {
        DataValuePointCode[] other = { null };
        
        try
        {
            if (pcFormat == PCFormat.ePCANSI)
            {
                other[0].setANSI();
                other[0] = new DataValuePointCode(DataValueType.ePointCode, pcValue.toString());
            }
            else if (pcFormat == PCFormat.ePCITU)
            {
                other[0].setITU();
                other[0] = new DataValuePointCode(DataValueType.ePointCode, pcValue.toString());
            }
            else if(pcFormat == PCFormat.ePCDecimal)
            {
            	other[0].setDecimal();
            	other[0] = new DataValuePointCode(DataValueType.ePointCode, longValue);
            }
            
            copy(other);
        }
        catch (InvalidDataTypeException e)
        {
            other[0] = null;
        }
        
        return other[0];
    }
    
    public void setDecimal() 
    {
    	pcFormat = PCFormat.ePCDecimal;
	}

	public void setITU() 
    {
    	pcFormat = PCFormat.ePCITU;
	}

	public void setANSI() 
    {
    	pcFormat = PCFormat.ePCANSI;
	}

	public long toInteger()
    {
		long val = 0;
		
		if(pcFormat == PCFormat.ePCDecimal)
		{
			val = longValue;
		}
		else if (pcFormat == PCFormat.ePCITU)
		{
			val = getPC383(pcValue[0], pcValue[1], pcValue[2]);
		}
		else if (pcFormat == PCFormat.ePCANSI)
		{
			val = getPC888(pcValue[0], pcValue[1], pcValue[2]);
		}
		
		return val;
    }
    
    public boolean toBoolean()
    {
        return (toInteger() != 0);
    }

    public String toUnformattedString()
    {
        String result = "";
        
        switch (pcFormat)
        {
            case ePCANSI:
                result = String.format("%d-%d-%d",
                                       (int) (pcValue[0] & 0x000000FF),
                                       (int) (pcValue[1] & 0x000000FF),
                                       (int) (pcValue[2]& 0x000000FF));
                break;
            
            case ePCITU:
            	
                result = String.format("%d-%d-%d",
                                       (int) (pcValue[0] & 0x000000FF),
                                       (int) (pcValue[1] & 0x000000FF),
                                       (int) (pcValue[2] & 0x000000FF));
                break;

            case ePCDecimal:
            	result = new Long(longValue).toString();
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
            generatedHashCode = (int) (toInteger() & longValue);
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
        else
            return toInteger() == ((DataValue) other).toInteger();
    }
    
    public boolean lessThan(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return false;
        else
            return toInteger() < ((DataValue) other).toInteger();
    }
    
    public byte [] asByteArray()
    {
    	byte[] bArray = null;
    	
    	switch(pcFormat)
    	{
    		case ePCANSI:
    		case ePCITU:
    		{
    			bArray = new byte[3];
    			bArray[0] = (byte)pcValue[0];
    			bArray[1] = (byte)pcValue[1];
    			bArray[2] = (byte)pcValue[2];
     		}
    		break;
    			
    		case ePCDecimal:
    		{
    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
    			DataOutputStream dos = new DataOutputStream(bos);
    			try 
    			{
					dos.writeLong(longValue);
				}
    			catch (IOException e) 
    			{
					LoggerManager.getLogger().error("Unable to create byte array from long");
				}
    			
    			bArray = bos.toByteArray();
    		}
    		break;
    	}
        return bArray;
    }
    
	@Override
	public byte[] toFidrBinary() 
	{
		ByteBuffer buffer = null;
		/*
		 * Format : NTLddddd...
		 * N : dataTypeNumber - 17 (0x11)
		 * T : Type { 1- ANSI, 2 - ITU, 3 - Decimal }
		 * L : length of the following data
		 * ddd : actual data
		 */
		switch(pcFormat)
		{
			case ePCANSI:
			{
				int[] pcDigits = new int[3];
				pcDigits[0] = (int) (pcValue[0] & 0x000000FF);
				pcDigits[1] = (int) (pcValue[1] & 0x000000FF);
				pcDigits[2] = (int) (pcValue[2] & 0x000000FF);
		
				buffer = ByteBuffer.allocate(6);
				UnsignedInts.putUnsignedByte(buffer, 0x11);
				UnsignedInts.putUnsignedByte(buffer, 1);
				UnsignedInts.putUnsignedByte(buffer, 3);
				for (int i = 0; i < pcDigits.length; i++) 
				{
					UnsignedInts.putUnsignedByte(buffer, pcDigits[i]);
				}
				
			}
			break;
			
			case ePCITU:
			{
				int[] pcDigits = new int[3];
				pcDigits[0] = (int) (pcValue[0] & 0x000000FF);
				pcDigits[1] = (int) (pcValue[1] & 0x000000FF);
				pcDigits[2] = (int) (pcValue[2] & 0x000000FF);
		
				buffer = ByteBuffer.allocate(6);
				UnsignedInts.putUnsignedByte(buffer, 0x11);
				UnsignedInts.putUnsignedByte(buffer, 2);
				UnsignedInts.putUnsignedByte(buffer, 3);
				for (int i = 0; i < pcDigits.length; i++) 
				{
					UnsignedInts.putUnsignedByte(buffer, pcDigits[i]);
				}
			}
			break;
			
			case ePCDecimal:
			{
				int len=0;
				if(longValue<Byte.MAX_VALUE)
				{
					len=1;
					buffer= ByteBuffer.allocate(len + 3);
					UnsignedInts.putUnsignedByte(buffer, 0x11);
					UnsignedInts.putUnsignedByte(buffer, 3);
					UnsignedInts.putUnsignedByte(buffer, len);
					UnsignedInts.putUnsignedByte(buffer,(int)longValue);
				}
				else if(longValue<Short.MAX_VALUE)
				{
					len=2;
					buffer= ByteBuffer.allocate(len + 3);
					UnsignedInts.putUnsignedByte(buffer, 0x11);
					UnsignedInts.putUnsignedByte(buffer, 3);
					UnsignedInts.putUnsignedByte(buffer, len);
					UnsignedInts.putUnsignedShort(buffer,(int)longValue);
				}
				else if(longValue<Integer.MAX_VALUE)
				{
					len=4;
					buffer= ByteBuffer.allocate(len + 3);
					UnsignedInts.putUnsignedByte(buffer, 0x11);
					UnsignedInts.putUnsignedByte(buffer, 3);
					UnsignedInts.putUnsignedByte(buffer, len);
					UnsignedInts.putUnsignedInt(buffer,(int)longValue);
				}
				else if(longValue<Long.MAX_VALUE)
				{
					len=8;
					buffer= ByteBuffer.allocate(len + 3);
					UnsignedInts.putUnsignedByte(buffer, 0x11);
					UnsignedInts.putUnsignedByte(buffer, 3);
					UnsignedInts.putUnsignedByte(buffer, len);
					UnsignedInts.putUnsignedLong(buffer,longValue);
				}
			}
			break;
		}
		
		return buffer.array();
	}
}
