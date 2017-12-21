package com.jm.wf.common.datavalues;

import java.nio.ByteBuffer;

import com.jm.wf.common.utils.UnsignedInts;

public class DataValueNull extends DataValueBase
{

    public DataValueNull() throws InvalidDataTypeException
    {
        super(DataValueType.eNull);
    }

    public DataValueNull(DataValueType type) throws InvalidDataTypeException
    {
        super(type);
    }
    
    public DataValue newInstance()
    {
        DataValueNull[] other = { null };
        
        try
        {
            other[0] = new DataValueNull(DataValueType.eNull);
            
            copy(other);
        }
        catch (InvalidDataTypeException e)
        {
            other[0] = null;
        }
        
        return other[0];
    }

    public boolean equals(Object other)
    {
        if (other == null)
            return false;
        else if (other instanceof DataValueNull)
            return true;
        else
            return false;
    }

    public boolean lessThan(Object other)
    {
        return false;
    }
    
    public byte [] asByteArray()
    {
        return null;
    }

	@Override
	public byte[] toFidrBinary() 
	{
		// allocate buffer 8 bytes for real and 1 for datatype
		ByteBuffer buffer = ByteBuffer.allocate(1);
		UnsignedInts.putUnsignedByte(buffer, 0x0D);
		return buffer.array();
	}
}
