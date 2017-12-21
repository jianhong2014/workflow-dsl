package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueOctetString extends DataValueBase
{
    protected ByteBuffer octetStrValue = null;
    protected boolean    asTBCD        = false;
    
    protected boolean    noPadding = false;
     
    static final String hexDigits = "0123456789abcdef";
    
    DataValueOctetString() throws InvalidDataTypeException
    {
        super(DataValueType.eOctetString);
    }
    
    DataValueOctetString(DataValueType type, byte [] value, int len, boolean tbcd)
        throws InvalidDataTypeException
    {
        super(type);
        
        asTBCD = tbcd;
        
        octetStrValue = ByteBuffer.wrap(value, 0, len);
    }
     
    DataValueOctetString(DataValueType type, byte [] value, int len, boolean tbcd,boolean noPadding)
       throws InvalidDataTypeException
    {
    	this(type, value, len, tbcd); 
    	this.noPadding = noPadding;
    	
         
    }
    
    DataValueOctetString(DataValueType type, byte [] value)
        throws InvalidDataTypeException
    {
        this(type, value, value.length, false);
    }

    DataValueOctetString(DataValueType type, byte [] value, int len)
        throws InvalidDataTypeException
    {
        this(type, value, len, false);
    }

    DataValueOctetString(DataValueType type, String value, boolean tbcd)
        throws InvalidDataTypeException
    {
        super(type);
        
        asTBCD = tbcd;
        
        StringBuilder tmpStr = new StringBuilder();
        
        if (value.length() % 2 == 0)
        {
            tmpStr.append(value.toLowerCase());
        }
        else
        {
            if (tbcd)
            {
                tmpStr.append(value.toLowerCase().substring(0, value.length() - 2));
                tmpStr.append("f");
                tmpStr.append(value.toLowerCase().substring(value.length() - 2));
            }
            else
                tmpStr.append("0").append(value.toLowerCase());
        }
        
        boolean matchOK = tbcd ? tmpStr.toString().matches("[0-9f]*")
                               : tmpStr.toString().matches("[0-9a-f]*");
        
        if (matchOK)
        {
            int bufLen = (tmpStr.length() + 1) / 2;
            
            octetStrValue = ByteBuffer.allocate(bufLen);
            
            for (int i = 0; i < bufLen; i++)
            {
                int i1 = i * 2;
                int i2 = i1 + 1;
                
                int val1 = 0;
                int val2 = 0;
                
                val1 = hexDigits.indexOf(tmpStr.charAt(i1));
                
                if (i2 < tmpStr.length())
                {
                    val2 = hexDigits.indexOf(tmpStr.charAt(i2));
                }
                
                octetStrValue.put(i, (byte) ((val1 * 16) + val2));
            }
        }
        else
            throw new InvalidDataTypeException(value + " is not a valid " + type);
    }
    
    DataValueOctetString(DataValueType type, String value)
        throws InvalidDataTypeException
    {
        this(type, value, false);
    }
    
    public DataValue newInstance()
    {
        DataValueOctetString[] other = { null };
        
        try
        {
            other[0] = new DataValueOctetString(DataValueType.eOctetString,
                                                 octetStrValue.array(),
                                                 octetStrValue.limit(),
                                                 asTBCD);
            
            copy(other);
        }
        catch (InvalidDataTypeException e)
        {
            other[0] = null;
        }
        
        return other[0];
    }
    
    public String toUnformattedString()
    {
        StringBuilder fmtStr = new StringBuilder();
        
        for (int i = 0; i < octetStrValue.limit(); i++)
        {
            int hi = (int) ((octetStrValue.get(i) & 0x000000f0) >>> 4);
            int lo = (int) (octetStrValue.get(i) & 0x0000000f);
            
            if (!asTBCD)
            {
                fmtStr.append(hexDigits.charAt(hi));
                fmtStr.append(hexDigits.charAt(lo));
            }
            else
            {
                if(!noPadding)
                {
                  if (lo != 0xf)
                       fmtStr.append(hexDigits.charAt(lo));
                
                   if (hi != 0xf)
                       fmtStr.append(hexDigits.charAt(hi));
                }
                else
                {
                       fmtStr.append(hexDigits.charAt(lo));
                       fmtStr.append(hexDigits.charAt(hi));
                } 
            }
        }
        
        return fmtStr.toString();
    }
    
    public String toString()
    {
        return toUnformattedString();
    }
    
    public byte [] asByteArray()
    {
        return octetStrValue.array();
    }   
    
    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other == this)
            return true;
        else
            return toString().equalsIgnoreCase(((DataValue) other).toUnformattedString());
    }
    
    public boolean lessThan(Object other)
    {
        return toString().compareToIgnoreCase(((DataValue) other).toUnformattedString()) < 0;
    }

	@Override
	public byte[] toFidrBinary() 
	{

		byte[] octectByteArray = octetStrValue.array();
		int len = octectByteArray.length;
		int offset = 0;

		//allocate buffer for length of string and
		//2bytes for length 1 byte for data type
		ByteBuffer buffer = ByteBuffer.allocate(len + 3);

		UnsignedInts.putUnsignedByte(buffer, 0x08);
		offset = offset + 1;

		UnsignedInts.putUnsignedShort(buffer, len);
		offset = offset + 2;

		System.arraycopy(octectByteArray, 0, buffer.array(), offset, len);

		return buffer.array();
	}
    
}
