/**
 * LoggerManager
 * 
 * This class acts as a manager for all Logger objects. The various CAAF processes
 * make use of this manager to create and retrieve a logger as required. The actual
 * logging uses the Apache log4j logging package.
 */
package com.jm.wf.common.logging;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.jm.wf.common.concurrent.JmThread;

public final class LoggerManager
{
	private static Logger logger = Logger.getRootLogger();
	
	private static String filename = System.getProperty("caaf.logging");
	
	private static boolean initialise = false;
	
	private static String instance = null;
	
	private static Map<String, Logger> loggerCache = new HashMap<String, Logger>();
    
    public static synchronized void initialise(String i) throws LoggerException
    {	
    	instance = i;
    	
    	File configFile = null;
    	
    	if (filename != null)
    	{
    		configFile = new File(filename);
    	}
    	else
    	{
    		throw new LoggerException("Please set JVM parameter: -Dcaaf.logging");
    	}
		
    	if (configFile != null)
    	{
			if (!configFile.exists())
			{
				throw new LoggerException(filename+" does not exist");
			}
    	}
    	else
    	{
    		throw new LoggerException(filename+" does not exist");
    	}

    	DOMConfigurator.configure(configFile.toString());

		// Print an initialisation message to allow appending loggers to have separation in the log files.
		Enumeration<?> loggers = LogManager.getCurrentLoggers();
    	if (loggers != null)
    	{
    		while (loggers.hasMoreElements())
    		{
    			Logger l = (Logger)loggers.nextElement();
    			
    			l.info("Logger "+l.getName()+" initialised");
    		}
    	}
		
		initialise = true;
    }
    
    public static synchronized void reset()
    {
    	loggerCache.clear();
    }
    
    public static synchronized Logger getLogger()
    {	
    	// Only look for loggers if we are initialised. Else return a root logger
    	if (initialise)
    	{
    		// The current thread executing this method provides the key (thread name) to perform
    		// a logger lookup. 
	    	String name = Thread.currentThread().getName();

	    	String child = null;
	
	    	// Look for a logger until
	    	// - We find a logger
	    	// - We find an orphan (no logger or parent)
	    	// - We reach the main thread (or its renamed version)
	    	do
	    	{
	    		// First check the cache for a quick logger find
	        	Logger log = loggerCache.get(name);
	    		
	        	// If there is no cached logger, try to find one
	        	if (log == null)
	        	{
	        		// Look through the loggers defined in the log4j xml
			    	Enumeration<?> loggers = LogManager.getCurrentLoggers();
			    	if (loggers != null)
			    	{
			    		while (loggers.hasMoreElements())
			    		{
			    			Logger l = (Logger)loggers.nextElement();
			    			
			    			// If the current thread name matches a logger, success
			    			if (l.getName().compareToIgnoreCase(name)==0)
			    			{
			    				// If the thread is not a child, cache the logging relationship
			    				if (child == null)
			    				{
			    					loggerCache.put(name, l);
			    				}
			    				// If the thread is a child, cache the child logging relationship
			    				else
			    				{
			    					loggerCache.put(child, l);
			    					child = null;
			    				}
			    				return l;
			    			}
			    		}
			    		// If we get here try to find the parent (creator) of the current thread
			    		child = name;
			    		try
			    		{
			    			name = ((JmThread) Thread.currentThread()).getParent();
			    		}
			    		catch (Exception e)
			    		{
			    			// There was no parent, so make name=child so we just return a root logger
				    		name = child;
			    		}
			    		
			    		// If there was no parent logger defined, we have an orphan thread so just return the root logger
			    		if (name.equals(child))
			    		{
			    			return logger;
			    		}
			    		
			    	}
	        	}
	        	// We found a parent thread and this parent had a logger in the cache
	        	else
	        	{	
	        		// Cache the child relationship for the parent logger
	        		if (child != null)
	        		{
	        			loggerCache.put(child, log);
	        		}	
	        		return log;
	        	}
	        	
	    	} while (name.compareToIgnoreCase(instance) != 0 && name.compareToIgnoreCase("main") != 0);
	    	
	    	return logger;
    	}
    	else
    	{
    		return logger;
    	}	
    }
}