package com.jm.wf.common.datavalues;



public abstract class DataValueBase implements DataValue
{
    protected DataValueType dataType           = DataValueType.eUnknown;
    protected int           truncationInterval = 0;
    protected String        formatString       = "";
    protected int           generatedHashCode  = Integer.MAX_VALUE;
    
    protected DataValueBase() throws InvalidDataTypeException
    {
        dataType = DataValueType.eUnknown;
    }
    
    protected DataValueBase(DataValueType type) throws InvalidDataTypeException
    {      
        dataType = type;
    }
    
    public DataValueType getDataValueType() 
    {
    	return (dataType);
    }
    
    public final void setTruncationInterval(int truncationInterval)
    {
        this.truncationInterval = truncationInterval;
    }
    
    public void setFormatString(String fmtStr)
    {
        formatString = fmtStr;
    }
    
    public String getFormatString()
    {
        return formatString;
    }
    
    public abstract DataValue newInstance();
    
    protected void copy(DataValueBase[] other)
    {
        other[0].setTruncationInterval(truncationInterval);
        
        other[0].setFormatString(formatString);
        
        other[0].hashCode(generatedHashCode);
    }
    
    public long toInteger()
    {
        return 0;
    }
    
    public double toReal()
    {
        return 0.0;
    }
    
    public boolean toBoolean()
    {
        return false;
    }
    
    public String toUnformattedString()
    {
        return "";
    }
    
    public String toString()
    {
        return "";
    }
    
    public abstract boolean equals(Object other);
    
    public abstract boolean lessThan(Object other);
    
    public void hashCode(int hc)
    {
        generatedHashCode = hc;
    }
    
    public int hashCode()
    {
        if (generatedHashCode == Integer.MAX_VALUE)
        {
            generatedHashCode = toString().hashCode();
        }
        return generatedHashCode;
    }
    
    public int size()
    {
        // Most DataValue objects have a size of 1 as indicated by this base class implementation.
        // Lists and sets will return the number of member elements. DataValueNull will return a
        // size of 0.
        
        return 1;
    }
    
    public int length()
    {
        // Most DataValue objects have a length of 1 as indicated by this base class implementation.
        // Lists and sets will return the number of member elements. Strings will return the number
        // of characters in the string. DataValueNull will return a length of 0.
        
        return 1;
    }
}
