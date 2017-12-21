package com.jm.wf.common.utils;

public class ConfigurationParsingException extends Exception
{
    static final long serialVersionUID = 0L;
    
    public ConfigurationParsingException()
    {
        super();
    }

    public ConfigurationParsingException(String descr)
    {
        super(descr);
    }

    public ConfigurationParsingException(Exception e)
    {
        super(e);
    }
}
