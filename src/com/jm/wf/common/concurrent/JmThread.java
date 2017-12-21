package com.jm.wf.common.concurrent;

public class JmThread extends Thread
{
	private String parent = null;
	
	public JmThread()
	{
		super();
		
		setParent(Thread.currentThread().getName());
	}

	public void setParent(String parent)
	{
		this.parent = parent;
	}
	
	public String getParent()
	{
		return parent;
	}
}
