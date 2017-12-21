package com.jm.wf.events.filters;

public class InvalidFilterIntervalException extends Exception
{
    static final long serialVersionUID = 0L;
    
    public InvalidFilterIntervalException()
    {
        super();
    }

    public InvalidFilterIntervalException(String descr)
    {
        super(descr);
    }

    public InvalidFilterIntervalException(Exception e)
    {
        super(e);
    }
}
