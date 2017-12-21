package com.jm.wf.common.utils;

public class ConfigurationInvalidDataException extends Exception
{
    static final long serialVersionUID = 0L;
    
	public ConfigurationInvalidDataException ()
	{
		super ();
	}
	
	public ConfigurationInvalidDataException (String desc)
	{
		super (desc);
	}
	
	public ConfigurationInvalidDataException (Exception e)
	{
		super (e);
	}
}
