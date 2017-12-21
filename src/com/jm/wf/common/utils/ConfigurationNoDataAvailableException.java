package com.jm.wf.common.utils;

public class ConfigurationNoDataAvailableException extends Exception 
{
    static final long serialVersionUID = 0L;
    
	public ConfigurationNoDataAvailableException ()
	{
		super ();
	}
	
	public ConfigurationNoDataAvailableException (String desc)
	{
		super (desc);
	}
	
	public ConfigurationNoDataAvailableException (Exception e)
	{
		super (e);
	}
}
