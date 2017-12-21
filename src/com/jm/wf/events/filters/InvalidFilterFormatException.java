package com.jm.wf.events.filters;

public class InvalidFilterFormatException extends Exception
{
    static final long serialVersionUID = 0L;
    
    public InvalidFilterFormatException()
    {
        super();
    }

    public InvalidFilterFormatException(String descr)
    {
        super(descr);
    }

    public InvalidFilterFormatException(Exception e)
    {
        super(e);
    }
}
