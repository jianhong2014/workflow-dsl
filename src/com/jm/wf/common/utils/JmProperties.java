package com.jm.wf.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.jm.wf.common.logging.LoggerManager;

/**
 * This class adds the ability to convert the result of a getProperties to an int or a long
 */
public class JmProperties extends Properties
{
    static final long serialVersionUID = 0L;
    
    private String fileName = null; 
    private String instance = null;
    private String prefix = null;
    
    public JmProperties()
    {                
    }
        
    /**
     * Constructor allowing the properties prefix and instance to be defined for later use.
     * 
     * @param fileName
     * @param prefix
     * @param instance
     */
    public JmProperties(String fileName, String prefix, String instance) throws FileNotFoundException, IOException
    {
        FileInputStream propStream = new FileInputStream (fileName);
        
        this.fileName = fileName;
        this.prefix = prefix;
        this.instance = instance;
        
        this.load(propStream);
        
        propStream.close();
    }
    
    /**
     * Constructor to copy old properties.
     * 
     * @param oldProps
     * @param prefix
     * @throws FileNotFoundException
     * @throws IOException
     */
    public JmProperties(JmProperties oldProps, String prefix) throws FileNotFoundException, IOException
    {
        String fileName = oldProps.fileName;
        
        FileInputStream propStream = new FileInputStream (fileName);
        
        this.fileName = fileName;
        this.prefix = prefix;
        this.instance = null;
        
        this.load(propStream);
        
        propStream.close();
    }

    /**
     * Constructor
     * 
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
     */
    public JmProperties(String fileName) throws FileNotFoundException, IOException
    {
        this.fileName = fileName;
        
        FileInputStream propStream = new FileInputStream (fileName);
        
        this.load(propStream);
        
        propStream.close();
    }

    public JmProperties(Map<String,String> propertyMap)
    {
    	this.putAll(propertyMap);
    }
    
    /**
     * Returns file name of the properties file.
     * 
     * @return File Name
     */
    public String getFileName ()
    {
        return this.fileName;
    }

    /**
     * Returns the prefix used to construct a fully qualified property name.
     * 
     * @return Prefix
     */
    public String getPrefix ()
    {
        return this.prefix;
    }

    /**
     * Returns the instance name used to construct a fully qualified property name.
     * 
     * @return Instance Name
     */
    public String getInstance ()
    {
        return this.instance;
    }

    /**
     * Creates a key from a previously defined prefix and
     * instance name.
     * 
     * @param section
     * @return String
     */
    private String expand (String section, String key)
    {
        StringBuilder newKey = new StringBuilder(100);
        
        if (prefix != null)
        {
            newKey.append(prefix);
            newKey.append(".");
        }
        
        if (instance != null)
        {
            newKey.append(instance);
            newKey.append(".");
        }
        
        if (section != null)
        {
            newKey.append(section);
            newKey.append(".");
        }
        
        if (key != null)
        {
            newKey.append(key);
        }
        
        return (newKey.toString());
    }
    
    /**
     * Returns a string from the properties with an exception and log error
     * on failure.
     * 
     * @param section
     * @param key
     * @return String
     * @throws ConfigurationNoDataAvailableException
     */
    public String getPropertyString(String section, String key)
                                        throws ConfigurationNoDataAvailableException
    {
        String value = this.getProperty(expand(section, key));
        
        if (value != null)
        {
            value = expandValue(value.trim());
        }
        else
        {
            String errMsg = "Property " + expand(section, key) + " not found";
            if (LoggerManager.getLogger().isDebugEnabled())
            {
                LoggerManager.getLogger().debug(errMsg);
            }
            throw new ConfigurationNoDataAvailableException(errMsg);
        }
     
        return (value);
    }    
    
    /**
     * Version of getPropertyString that does not perform variable interpolation.
     * 
     * @param section
     * @param key
     * @return String
     * @throws ConfigurationNoDataAvailableException
     */
    public String getRawPropertyString(String section, String key)
        throws ConfigurationNoDataAvailableException
    {
        String value = this.getProperty(expand(section, key));

        if (value == null)
        {
            String errMsg = "Property " + expand(section, key) + " not found";
            LoggerManager.getLogger().debug(errMsg);
            throw new ConfigurationNoDataAvailableException(errMsg);
        }

        return (value);
    }    

    /**
     * Version of getPropertyString where no section is provided.
     * 
     * @param key
     * @return String
     * @throws ConfigurationNoDataAvailableException
     */
    public String getPropertyString (String key)
        throws ConfigurationNoDataAvailableException
    {
        return (getPropertyString(null, key));
    }

    /**
     * Version of getRawPropertyString where no section is provided.
     * 
     * @param key
     * @return String
     * @throws ConfigurationNoDataAvailableException
     */
    public String getRawPropertyString (String key)
        throws ConfigurationNoDataAvailableException
    {
        return (getRawPropertyString(null, key));
    }

    /**
     * Boolean property.
     * 
     * @param section
     * @param key
     * @return boolean
     * @throws ConfigurationNoDataAvailableException
     */
    public boolean getPropertyBool (String section, String key) 
                        throws ConfigurationNoDataAvailableException, ConfigurationParsingException
    {
        Boolean result = false;
        String  value = getPropertyString (section, key);
        
        if (value.equalsIgnoreCase("true") ||
            value.equals("1") ||
            value.equalsIgnoreCase("on") ||
            value.equalsIgnoreCase("yes"))
        {
            result = true;
        }
        else if (value.equalsIgnoreCase("false") ||
                 value.equals("0") ||
                 value.equalsIgnoreCase("off") ||
                 value.equalsIgnoreCase("no"))
        {
            result = false;
        }
        else
        {
            throw new ConfigurationParsingException ("Non-boolean value: " + value);
        }
        
        return (result);
    }     
    
    /**
     * Boolean with no section name.
     * 
     * @param key
     * @return boolean
     * @throws ConfigurationNoDataAvailableException
     * @throws ConfigurationParsingException
     */
    public boolean getPropertyBool (String key) 
                        throws ConfigurationNoDataAvailableException, ConfigurationParsingException
    {
        return (getPropertyBool(null, key));
    }     
    
    /**
     * Returns an integer and checks for existance of the property and that its
     * a valid number. Throws exceptions on errors.
     * 
     * @param section
     * @param key
     * @return int
     * @throws NumberFormatException
     * @throws ConfigurationNoDataAvailableException
     */
    public int getPropertyInt (String section, String key) throws NumberFormatException, ConfigurationNoDataAvailableException
    {
        String valueStr = getPropertyString (section, key);
        int    value = 0;
        
        try
        {
            value = Integer.parseInt (valueStr.trim());
        }
        catch (Exception e)
        {
            String errMsg = "Property " + expand(section, key) + " invalid number: " + valueStr;
            LoggerManager.getLogger().debug(errMsg);
            throw new NumberFormatException(errMsg);
        }
        
        return (value);
    }
    
    /**
     * For use with no section names.
     * 
     * @param key
     * @return int
     * @throws NumberFormatException
     * @throws ConfigurationNoDataAvailableException
     */
    public int getPropertyInt (String key) throws NumberFormatException, ConfigurationNoDataAvailableException
    {
        return (getPropertyInt(null, key));
    }

   
    /**
     * Returns a long and checks for existance of the property and that its
     * a valid number. Throws exceptions on errors.
     * 
     * @param section
     * @param key
     * @return long
     * @throws NumberFormatException
     * @throws ConfigurationNoDataAvailableException
     */
    public long getPropertyLong (String section, String key) throws NumberFormatException, ConfigurationNoDataAvailableException
    {
        String valueStr = getPropertyString (section, key);
        long   value = 0;
        
        try
        {
            value = Long.parseLong (valueStr.trim());
        }
        catch (Exception e)
        {
            String errMsg = "Property " + expand(section, key) + " invalid number: " + valueStr;
            LoggerManager.getLogger().debug(errMsg);
            throw new NumberFormatException(errMsg);
        }  
        
        return (value);
    }
    
    /**
     * For use with no section names.
     * 
     * @param key
     * @return long
     * @throws NumberFormatException
     * @throws ConfigurationNoDataAvailableException
     */
    public long getPropertyLong (String key) throws NumberFormatException, ConfigurationNoDataAvailableException
    {
        return (getPropertyLong(null, key));
    }
      
    /**
     * Converts a string comma separated list of items into an array list.
     * 
     * @param itemList
     * @return ArrayList<String>
     */
    public ArrayList<String> getConfigItems (String itemList)
    {
        ArrayList<String> list = new ArrayList<String>(); 
        String []         splitString = null;
                
        if (itemList != null)
        {
            splitString = itemList.split(",");
                                
            if (splitString[0].equals (list))
            {
                list.add(splitString[0]);
            }
            else
            {
                for (int i = 0 ; i < splitString.length ; i++)
                {
                    list.add(splitString[i]);
                }
            }
        }
        
        return (list);
    }    
    
    /**
     * Expands system and environment variables provided in a value string.
     * 
     * System properties are provided via the "-D" command line while
     * environment variables are from the system environment. 
     * 
     * For full string value use $VARIABLE
     * If mixed with other text use ${VARIABLE}...
     * Also allows for ...${VARIABLE}...${VARIABLE}...
     * 
     * @param value
     * @return String
     */
    public static String expandValue (String value)
    {
        String valuePortion = null;
        String completeValue = "";                
        int    dollarBracePos = value.indexOf ("${");
        int    consumed = 0; 
        int    closingBracePos = 0;
        
        if (value.startsWith("$") && value.charAt(1) != '{')
        {
        	/*
        	 * Expand a string which starts with a dollar.
        	 */
        	completeValue = expandVariable(value.substring(1, value.length()));        	
        }
        else if (dollarBracePos != -1)
        {
        	/*
             * Expand all occurrences of ${XX} with the appropriate
             * variable.
             */
        	while (dollarBracePos != -1)
            {
                /*
                 * Add any part of the string that preceded this expansion but 
				 * itself did not require expansion.
                 */
                if (dollarBracePos > consumed)
                {
                    if (consumed == 0)
                    {
                        completeValue += value.substring(consumed, dollarBracePos);
                    }
                    else
                    {
                        completeValue += value.substring((consumed + 1), dollarBracePos);
                    }
                }
                
                /*
                 * Locate the end of the variable and append its value.
                 */
                closingBracePos = value.indexOf ("}", dollarBracePos);
                
                valuePortion = expandVariable(value.substring((dollarBracePos + 2), closingBracePos));
                
                completeValue += valuePortion;
                
                consumed = closingBracePos;
                
                dollarBracePos = value.indexOf("${", consumed);
            }
        	
        	/*
             * Add any remaining portion (containing nothing to be expanded) 
             * at the end of the string.
             */
            if (value.length () > consumed + 1)
            {
                completeValue += value.substring (consumed + 1);
            }
        }
        else 
        {
        	/*
        	 * No dollars anywhere, just use the original value string.
        	 */
            completeValue = value;
        }
        
        return (completeValue);
    }
    
    /**
     * Expands the property or variable.
     * 
     * @param value
     * @return String
     */
    private static String expandVariable (String value)
    {
        String env_var = null;
        
        // Check the system properties
        env_var = System.getProperty(value);
        
        if (env_var != null)
        {
           value = env_var;
        }
        else
        {
            // Check the environment variables
            env_var = System.getenv(value);
            
            if (env_var != null)
            {
               value = env_var;
            }
        }
        
        return (value);
    }
    
    /**
     * This method reads thru the current configuration and checks if each of the keys exist
     * in the comparison object and if they have the same value.
     * @param value - the object to be compared
     * @return true - if the two objects are the same, false otherwise
     */
    public boolean equals( Object value )
    {
        boolean retVal = false;
        
        if( value instanceof JmProperties)
        {
            JmProperties thatProperties = (JmProperties) value;
            Object thisKey = null;
            String thatValue = null;

            // hope for the best
            retVal = true;
            
            // loop though the current configuration 
            for (Enumeration<?> thisElement = this.propertyNames(); thisElement.hasMoreElements(); )
            {
                thisKey = thisElement.nextElement();

                // try to locate the value for the key in the new configuration
                thatValue = thatProperties.getProperty( thisKey.toString() );
                
                if( thatValue != null )
                {
                    // compare the new value with the one from the current config
                    if( !thatValue.equals( this.getProperty( thisKey.toString() ) ) )
                    {
                        retVal = false;
                    }
                }
                else
                {
                    // the value does not appear in the new configuration
                    retVal = false;
                }
                
                if( !retVal )
                {
                    break;
                }
            }
        }
        return retVal;
    }
    
    public JmProperties getSubSection(String section)
    {
    	JmProperties secProp = new JmProperties();
    	
    	Set<Object> keySet = keySet();
    	
    	for(Object obj : keySet)
    	{
    		if(obj instanceof String)
    		{
    			String strKey = (String)obj;
    			if (strKey.startsWith(section))
    			{
    				String value = this.getProperty(strKey);
    				String newKey = strKey.substring(section.length() + 1);
    				secProp.put(newKey, value);
    			}
    		}
    	}
    	
    	return secProp;
    }
}

/*** END OF FILE ***/
