package com.jm.wf.common.concurrent;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public abstract class JmTimerTask extends TimerTask
{
	Logger logger = null;
	
	public JmTimerTask(Logger l)
	{
		super();
		
		logger = l;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
}
