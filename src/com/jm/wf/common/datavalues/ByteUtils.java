package com.jm.wf.common.datavalues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ByteUtils 
{
	
	public static byte[] collectionToBinary(Collection values) 
	{
		int arraySize = 0;
		
		List byteArrays = new ArrayList<byte[]>();

		for (Iterator iterator = values.iterator(); iterator.hasNext();) 
		{
			DataValue entry = (DataValue) iterator.next();

			byte[] fidrBinary = getFidrBinary(entry);
			
			byteArrays.add(fidrBinary); // adding all the byte arrays in a list
			                            // for merging all into a single byte[]
			
			arraySize = arraySize + fidrBinary.length;
		}
		
		byte[] byteBuffer = new byte[arraySize];
		
		int byteArraysLength = byteArrays.size();
		
		int byteBufferLength=0;

		for (int i = 0; i < byteArraysLength; i++) 
		{
			byte[] byteArray = (byte[]) byteArrays.get(i);
			System.arraycopy(byteArray, 0, byteBuffer, byteBufferLength,
					byteArray.length);
			
			byteBufferLength+=byteArray.length;
		}
		return byteBuffer;
	}

	private static byte[] getFidrBinary(DataValue entry) 
	{
		byte[] fidrBinary = null;
		if (entry.getDataValueType() == DataValueType.eInteger) 
		{
			//incase of integer need to call different method
			// so that we get 8 byte integer.
			fidrBinary = ((DataValueInteger) entry)
					.toFidrBinaryForCollection();
		} else if((entry.getDataValueType() == DataValueType.eList))
		{
			DataValueList listData=(DataValueList) entry;
			fidrBinary=listData.toFidrBinary();
			
		}else if((entry.getDataValueType() == DataValueType.eSet))
		{
			DataValueSet setData=(DataValueSet) entry;
			fidrBinary=setData.toFidrBinary();
		}		
		else if ((entry.getDataValueType() == DataValueType.eMap))
		{
			DataValueMap mapEntry=(DataValueMap) entry;
			fidrBinary=mapEntry.toFidrBinary();
		}
		else
		{
			fidrBinary=entry.toFidrBinary();
		}
		return fidrBinary;
	}
	
	public static byte[] mapToBinary(Map<DataValue,DataValue> valueMap) 
	{

		int arraySize = 0;

		byte[] byteBuffer = new byte[arraySize];

		List byteArrays = new ArrayList<byte[]>();

		Set<DataValue> keys = valueMap.keySet();

		for (Iterator iterator = keys.iterator(); iterator.hasNext();) 
		{
			DataValue key = (DataValue) iterator.next();

			DataValue dataValue = valueMap.get(key);

			byte[] fidrBinaryForKey = getFidrBinary(key);
			byte[] fidrBinaryForValue = getFidrBinary(dataValue);

			byte[] keyAndValueByteArray = new byte[fidrBinaryForKey.length
					+ fidrBinaryForValue.length];

			System.arraycopy(fidrBinaryForKey, 0, keyAndValueByteArray, 0,
					fidrBinaryForKey.length);

			System.arraycopy(fidrBinaryForValue, 0, keyAndValueByteArray,
					fidrBinaryForKey.length, fidrBinaryForValue.length);

			byteArrays.add(keyAndValueByteArray);

			arraySize += fidrBinaryForKey.length + fidrBinaryForValue.length;
		}

		byteBuffer = new byte[arraySize];

		int byteArraysLength = byteArrays.size();

		int byteBufferLength=0;
		for (int i = 0; i < byteArraysLength; i++) 
		{
			byte[] byteArray = (byte[]) byteArrays.get(i);
			System.arraycopy(byteArray, 0, byteBuffer, byteBufferLength,
					byteArray.length);
			
			byteBufferLength += byteArray.length;
		}
		return byteBuffer;
	}
	

}
