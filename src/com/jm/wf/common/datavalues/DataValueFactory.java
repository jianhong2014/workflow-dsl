package com.jm.wf.common.datavalues;

import java.util.Collection;



public final class DataValueFactory
{
    public static DataValue createNullValue()
    {
        DataValue value = null;

        try
        {
            value = new DataValueNull();
        }
        catch (Exception e)
        {
            value = null;
        }
        
        return value;
    }
    
    public static DataValue createDataValue(Object dataValue, DataValueType dataType) {
    	 DataValue value = null;
    	 try {
        	 switch (dataType)
             {
                 case eInteger:
                     value = new DataValueInteger(dataType, (Integer)dataValue);
                 break;

                 case eReal:
                     value = new DataValueReal(dataType, (Double)dataValue);
                 break;

                 case eBoolean:
                     value = new DataValueBoolean(dataType, (Boolean)dataValue);
                 break;

                 case eString:
                     value = new DataValueString(dataType, (String)dataValue);
                 break;
                 case eUnknown:
                 default:
                     throw new InvalidDataTypeException();
             } 
    	 } catch (Exception exception) {
    		 exception.printStackTrace();
    	 }
    	 return value;
    }
    
    public static DataValue createDataValue(DataValue     dataValue,
                                            DataValueType dataType)
    {
        DataValue value = null;

        try
        {
            switch (dataType)
            {
                case eInteger:
                    value = new DataValueInteger(dataType, dataValue.toInteger());
                break;

                case eReal:
                    value = new DataValueReal(dataType, dataValue.toReal());
                break;

                case eBoolean:
                    value = new DataValueBoolean(dataType, dataValue.toBoolean());
                break;

                case eString:
                    value = new DataValueString(dataType, dataValue.toString());
                break;

                case eTimestamp:
                {
                    switch (dataValue.getDataValueType())
                    {
                        case eInteger:
                        case eBSID:
                        case eESN:
                            value = new DataValueTimestamp(dataType, dataValue.toInteger());
                            break;
                            
                        case eReal:
                        case eTimestamp:
                        case eDuration:
                            value = new DataValueTimestamp(dataType, dataValue.toReal());
                            break;
                            
                        default:
                            value = new DataValueTimestamp(dataType, dataValue.toString());
                    }
                }
                break;

                case eDuration:
                {
                    switch (dataValue.getDataValueType())
                    {
                        case eInteger:
                        case eBSID:
                        case eESN:
                            value = new DataValueDuration(dataType, dataValue.toInteger());
                            break;
                            
                        case eReal:
                        case eTimestamp:
                        case eDuration:
                            value = new DataValueDuration(dataType, dataValue.toReal());
                            break;
                            
                        default:
                            value = new DataValueDuration(dataType, dataValue.toString());
                    }
                }
                break;

                case eIPv4Address:
                {
                    switch (dataValue.getDataValueType())
                    {
                        case eInteger:
                        case eReal:
                        case eTimestamp:
                        case eDuration:
                        case eIPv4Address:
                        case eBSID:
                        case eESN:
                            value = new DataValueIPv4Address(dataType, dataValue.toInteger());
                            break;
                            
                        default:
                            value = new DataValueIPv4Address(dataType, dataValue.toString());
                    }
                }
                break;

                case eBSID:
                {
                    switch (dataValue.getDataValueType())                    
                    {
                        case eInteger:
                        case eReal:
                        case eTimestamp:
                        case eDuration:
                        case eIPv4Address:
                        case eBSID:
                        case eESN:
                            value = new DataValueBSID(dataType, dataValue.toInteger());
                            break;
                            
                        default:
                            value = new DataValueBSID(dataType, dataValue.toString());
                    }
                }
                break;

                case eESN:
                {
                    switch (dataValue.getDataValueType())
                    {
                        case eInteger:
                        case eReal:
                        case eTimestamp:
                        case eDuration:
                        case eIPv4Address:
                        case eBSID:
                        case eESN:
                            value = new DataValueESN(dataType, dataValue.toInteger());
                            break;
                            
                        default:
                            value = new DataValueESN(dataType, dataValue.toString());
                    }
                }
                break;

                case eAdHocService:
                    value = new DataValueAdHocService(dataType, dataValue.toString());
                break;

                case eOctetString:
                    value = new DataValueOctetString(dataType, dataValue.toString());
                break;

                case eStatusCode:
                    value = new DataValueStatusCode(dataType, dataValue.toString());
                break;
                
                case eList:
                {
                    DataValueCollection list = new DataValueList();
                    
                    if (dataValue instanceof DataValueCollection)
                    {
                        list.addAll((DataValueCollection) dataValue);
                    }
                    else if (dataValue instanceof DataValueMap)
                    {
                        DataValueMap map = (DataValueMap) dataValue;
                        
                        list.addAll(map.values());
                    }
                    else
                    {
                        list.add(dataValue);
                    }
                    
                    value = (DataValue) list;
                }
                break;

                case eSet:
                {
                    DataValueCollection set = new DataValueSet();
                    
                    if (dataValue instanceof DataValueCollection)
                    {
                        set.addAll((DataValueCollection) dataValue);
                    }
                    else if (dataValue instanceof DataValueMap)
                    {
                        DataValueMap map = (DataValueMap) dataValue;
                        
                        set.addAll(map.keys());
                    }
                    else
                    {
                        set.add(dataValue);
                    }
                    
                    value = (DataValue) set;
                }
                break;

                case eMap:
                {
                    if (dataValue instanceof DataValueMap)
                    {
                        DataValueMap map = (DataValueMap) dataValue;
                        
                        value = (DataValue) new DataValueMapBase(map.getValueMap());
                    }
                    else if (dataValue instanceof DataValueCollection)
                    {
                        DataValueMap map =
                            (DataValueMap) new DataValueMapBase(((DataValueCollection) dataValue).getValues());
                        
                        value = map;
                    }
                    else
                    {
                        value = new DataValueMapBase();
                    }
                }
                break;
                
                case eNull:
                    value = new DataValueNull();
                break;

                case eUnknown:
                default:
                    throw new InvalidDataTypeException();
            }
        }
        catch (InvalidDataTypeException e)
        {
            // Log something...

            value = null;
        }

        return value;
    }

    public static DataValue createDataValueCollection(DataValueType dataType)
    {
        DataValue value = null;

        try
        {
            switch (dataType)
            {
                case eList:
                    value = new DataValueList();
                break;

                case eSet:
                    value = new DataValueSet();
                break;

                case eMap:
                    value = new DataValueMapBase();
                break;

                default:
                    throw new InvalidDataTypeException();
            }
        }
        catch (InvalidDataTypeException e)
        {
            // Log something...

            value = null;
        }

        return value;
    }

    public static DataValue createDataValueCollection(DataValueType         dataType,
                                                      Collection<DataValue> values)
    {
        DataValue value = null;

        try
        {
            switch (dataType)
            {
                case eList:
                    value = new DataValueList(values);
                break;

                case eSet:
                    value = new DataValueSet(values);
                break;

                case eMap:
                    value = new DataValueMapBase(values);
                break;
                    
                default:
                    throw new InvalidDataTypeException();
            }
        }
        catch (InvalidDataTypeException e)
        {
            // Log something...

            value = null;
        }

        return value;
    }

    public static DataValue createDataValue(long          longValue,
                                            DataValueType dataType)
    {
        DataValue value = null;

        try
        {
            switch (dataType)
            {
                case eInteger:
                    value = new DataValueInteger(dataType, longValue);
                break;

                case eReal:
                    value = new DataValueReal(dataType, longValue);
                break;

                case eBoolean:
                    value = new DataValueBoolean(dataType, longValue != 0);
                break;

                case eString:
                    value = new DataValueString(dataType, longValue);
                break;

                case eTimestamp:
                    value = new DataValueTimestamp(dataType, longValue);
                break;

                case eDuration:
                    value = new DataValueDuration(dataType, longValue);
                break;

                case eIPv4Address:
                    value = new DataValueIPv4Address(dataType, longValue);
                break;

                case eBSID:
                    value = new DataValueBSID(dataType, longValue);
                break;

                case eESN:
                    value = new DataValueESN(dataType, longValue);
                break;

                case eAdHocService:
                    value = new DataValueAdHocService(dataType, longValue);
                break;

                case eNull:
                    value = new DataValueNull();
                break;
                
                case ePointCode:
                	value = new DataValuePointCode(dataType, longValue);
               	break;

                case eOctetString:
                case eStatusCode:
                case eUnknown:
                default:
                    throw new InvalidDataTypeException();
            }
        }
        catch (InvalidDataTypeException e)
        {
            // Log something...

            value = null;
        }

        return value;
    }

    public static DataValue createDataValue(double        realValue,
                                            DataValueType dataType)
    {
        DataValue value = null;

        try
        {
            switch (dataType)
            {
                case eInteger:
                    value = new DataValueInteger(dataType, realValue);
                break;

                case eReal:
                    value = new DataValueReal(dataType, realValue);
                break;

                case eBoolean:
                    value = new DataValueBoolean(dataType, realValue != 0.0);
                break;

                case eString:
                    value = new DataValueString(dataType, realValue);
                break;

                case eTimestamp:
                    value = new DataValueTimestamp(dataType, realValue);
                break;

                case eDuration:
                    value = new DataValueDuration(dataType, realValue);
                break;

                case eNull:
                    value = new DataValueNull();
                break;

                case eIPv4Address:
                case eOctetString:
                case eBSID:
                case eESN:
                case eAdHocService:
                case eStatusCode:
                case eUnknown:
                default:
                    throw new InvalidDataTypeException();
            }
        }
        catch (InvalidDataTypeException e)
        {
            // Log something...

            value = null;
        }

        return value;
    }

    public static DataValue createDataValue(String        strValue,
                                            DataValueType dataType)
    {
        DataValue value = null;

        if (strValue != null)
        {
            try
            {
                switch (dataType)
                {
                    case eInteger:
                        value = new DataValueInteger(dataType, strValue);
                    break;

                    case eReal:
                        value = new DataValueReal(dataType, strValue);
                    break;

                    case eBoolean:
                        value = new DataValueBoolean(dataType, strValue);
                    break;

                    case eString:
                        value = new DataValueString(dataType, strValue);
                    break;

                    case eTimestamp:
                        value = new DataValueTimestamp(dataType, strValue);
                    break;

                    case eDuration:
                        value = new DataValueDuration(dataType, strValue);
                    break;

                    case eIPv4Address:
                        value = new DataValueIPv4Address(dataType, strValue);
                    break;

                    case eOctetString:
                        value = new DataValueOctetString(dataType, strValue);
                    break;

                    case eBSID:
                        value = new DataValueBSID(dataType, strValue);
                    break;

                    case eESN:
                        value = new DataValueESN(dataType, strValue);
                    break;

                    case eAdHocService:
                        value = new DataValueAdHocService(dataType, strValue);
                    break;

                    case eStatusCode:
                        value = new DataValueStatusCode(dataType, strValue);
                    break;

                    case eNull:
                        value = new DataValueNull();
                    break;
                    
                    case ePointCode:
                    	value = new DataValuePointCode(dataType, strValue);
                   	break;

                    case eUnknown:
                    default:
                        throw new InvalidDataTypeException();
                }
            }
            catch (InvalidDataTypeException e)
            {
                // Log something...

                value = null;
            }
        }

        return value;
    }

    public static DataValue createDataValue(String        strValue,
                                            DataValueType dataType,
                                            boolean       asTBCD)
    {
        DataValue value = null;

        if (strValue != null)
        {
            try
            {
                if (!asTBCD)
                {
                    value = createDataValue(strValue, dataType);
                }
                else
                {
                    switch (dataType)
                    {
                        case eOctetString:
                            value = new DataValueOctetString(dataType, strValue, asTBCD);
                        break;

                        case eNull:
                            value = new DataValueNull();
                        break;

                        case eInteger:
                        case eReal:
                        case eBoolean:
                        case eString:
                        case eTimestamp:
                        case eDuration:
                        case eIPv4Address:
                        case eBSID:
                        case eESN:
                        case eAdHocService:
                        case eStatusCode:
                        case eUnknown:
                        default:
                            throw new InvalidDataTypeException();
                    }
                }
            }
            catch (InvalidDataTypeException e)
            {
                // Log something...

                value = null;
            }
        }

        return value;
    }

    public static DataValue createDataValue(long          longValue1,
                                            long          longValue2,
                                            DataValueType dataType)
    {
        DataValue value = null;

        try
        {
            switch (dataType)
            {
                case eTimestamp:
                    value = new DataValueTimestamp(dataType, longValue1, longValue2);
                break;

                case eDuration:
                    value = new DataValueDuration(dataType, longValue1, longValue2);
                break;

                case eAdHocService:
                    value = new DataValueAdHocService(dataType, longValue1, longValue2);
                break;

                case eStatusCode:
                    value = new DataValueStatusCode(dataType, longValue1, longValue2);
                break;

                case eNull:
                    value = new DataValueNull();
                break;

                case eInteger:
                case eReal:
                case eBoolean:
                case eString:
                case eIPv4Address:
                case eOctetString:
                case eBSID:
                case eESN:
                default:
                    throw new InvalidDataTypeException();
            }
        }
        catch (InvalidDataTypeException e)
        {
            // Log something...

            value = null;
        }

        return value;
    }

    public static DataValue createDataValue(byte[]        data,
                                            int           length,
                                            DataValueType dataType)
    {
        DataValue value = null;

        if (data != null)
        {
            try
            {
                switch (dataType)
                {
                    case eString:
                        value = new DataValueString(dataType, new String(data, 0, length));
                    break;

                    case eIPv4Address:
                        value = new DataValueIPv4Address(dataType, data);
                    break;

                    case eOctetString:
                        value = new DataValueOctetString(dataType, data, length);
                    break;

                    case eBSID:
                        value = new DataValueBSID(dataType, new String(data, 0, length));
                    break;

                    case eESN:
                        value = new DataValueESN(dataType, new String(data, 0, length));
                    break;

                    case eNull:
                        value = new DataValueNull();
                    break;
                    
                    case eList:
                    {
                    	value = (DataValueList) DataValueFactory.createDataValueCollection(dataType);
                    	
                    	for(byte dataByte : data)
                    	{
                    		((DataValueList)value).add(createDataValue(dataByte, DataValueType.eInteger));
                    	}
                    }
                    break;

                    case eInteger:
                    case eReal:
                    case eBoolean:
                    case eTimestamp:
                    case eDuration:
                    case eAdHocService:
                    case eStatusCode:
                    case eUnknown:
                    default:
                        throw new InvalidDataTypeException();
                }
            }
            catch (InvalidDataTypeException e)
            {
                // Log something...

                value = null;
            }
        }

        return value;
    }
    
    public static DataValue createDataValue(long[]        data,
    										int           length,
    										DataValueType outterDataType,
    										DataValueType innerDataType)
    {
    	DataValue value = null;

    	if (data != null)
    	{
    		try
    		{
    			switch (outterDataType)
    			{
	    			case eNull:
	    				value = new DataValueNull();
	    				break;
	
	    			case eList:
	    			{
	    				value = (DataValueList) DataValueFactory.createDataValueCollection(outterDataType);
	
	    				for(long dataByte : data)
	    				{
	    					((DataValueList)value).add(DataValueFactory.createDataValue(dataByte, innerDataType));
	    				}
	    			}
	    			break;
	
	    			case eESN:
	    			case eBSID:
	    			case eOctetString:
	    			case eIPv4Address:
	    			case eString:
	    			case eInteger:
	    			case eReal:
	    			case eBoolean:
	    			case eTimestamp:
	    			case eDuration:
	    			case eAdHocService:
	    			case eStatusCode:
	    			case eUnknown:
	    			default:
	    				throw new InvalidDataTypeException();
    			}
    		}
    		catch (InvalidDataTypeException e)
    		{
    			// Log something...

    			value = null;
    		}
    	}

    	return value;
    }

    public static DataValue createDataValue(byte[]        data,
                                            int           length,
                                            DataValueType dataType,
                                            boolean       asTBCD)
    {
        DataValue value = null;

        if (data != null)
        {
            try
            {
                switch (dataType)
                {
                    case eOctetString:
                        value = new DataValueOctetString(dataType, data, length, asTBCD);
                    break;

                    case eString:
                        if (asTBCD)
                        {
                            value = new DataValueString(dataType,
                                                        new DataValueOctetString(dataType, data, length, asTBCD).toString());
                        }
                        else
                        {
                            value = new DataValueString(dataType, new String(data, 0, length));
                        }
                    break;
                   
                    case eNoPadTelNo:
                           value = new DataValueString(dataType,
                            new DataValueOctetString(dataType, data, length, true,true).toString());
                            break;

                    case eNull:
                        value = new DataValueNull();
                    break;

                    case eInteger:
                    case eReal:
                    case eBoolean:
                    case eTimestamp:
                    case eDuration:
                    case eIPv4Address:
                    case eBSID:
                    case eESN:
                    case eAdHocService:
                    case eStatusCode:
                    case eUnknown:
                    default:
                        throw new InvalidDataTypeException();
                }
            }
            catch (InvalidDataTypeException e)
            {
                // Log something...

                value = null;
            }
        }

        return value;
    }
}
