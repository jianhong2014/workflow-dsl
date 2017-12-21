package com.jm.wf.common.logging;

public class LoggerException extends Exception
{
    static final long serialVersionUID = 0L;

    public LoggerException()
    {
        super();
    }

    public LoggerException(String descr)
    {
        super(descr);
    }

    public LoggerException(Exception e)
    {
        super(e);
    }
}
