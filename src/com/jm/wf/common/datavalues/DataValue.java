package com.jm.wf.common.datavalues;

public interface DataValue
{
    public DataValue newInstance();
    
    public void setTruncationInterval(int truncInterval);
    
    public void setFormatString(String fmtStr);
    
    public String getFormatString();
    
    public String toUnformattedString();
    
    public long toInteger();
    
    public double toReal();
    
    public boolean toBoolean();
    
    public boolean lessThan(Object other);
    
    public byte [] asByteArray();
    
    public int size();
    
    public int length();
    
    public DataValueType getDataValueType();
    
    public byte[] toFidrBinary();
}
