package com.jm.wf.common.datavalues;

public class InvalidDataTypeException extends Exception
{
    static final long serialVersionUID = 0L;
    
    public InvalidDataTypeException()
    {
        super();
    }
    
    public InvalidDataTypeException(String descr)
    {
        super(descr);
    }
    
    public InvalidDataTypeException(Exception e)
    {
        super(e);
    }
}
