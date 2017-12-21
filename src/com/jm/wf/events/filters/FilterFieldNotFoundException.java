package com.jm.wf.events.filters;

public class FilterFieldNotFoundException extends Exception
{
    static final long serialVersionUID = 0L;
    
    public FilterFieldNotFoundException()
    {
        super();
    }

    public FilterFieldNotFoundException(String descr)
    {
        super(descr);
    }

    public FilterFieldNotFoundException(Exception e)
    {
        super(e);
    }
}
