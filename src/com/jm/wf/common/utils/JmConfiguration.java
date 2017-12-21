package com.jm.wf.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

//
// Generic AHRTR configuration file reader which stores the key/value
// pairs for later retrieval as strings.  
//
public class JmConfiguration
{
    // Configuration data storage    

    private Map<String, Map<String,String>> m_config_map;
    private Map<String,String> m_section_map;
    private String m_section_name;
    
    //
    // main():
    // Allows the file parsing to be tested by passing a config file
    // name on the command line. Note that this option is for debug
    // only.
    // Normal use of this class is to use the readConfigFile(),
    // getConfigValue() and getConfigSection() methods.
    // 
    public static void main (String[] args) 
    {
        // Debug use only
        if (args.length > 0)
        {
            try
            {
                new JmConfiguration(args[0]).dumpConfig ();
            }
            catch (ConfigurationParsingException e)
            {
                System.out.println(e);
            }
        }
        else
        {
            System.out.println ("Error: No configuration filename supplied.");
        }
    }
         
    //
    // JmConfiguration():
    // Takes a valid config file name and passes each text line
    // to the parsing method.
    //
    public JmConfiguration(String config_file_name)
        throws ConfigurationParsingException
    {
        m_config_map = new HashMap<String, Map<String,String>> ();
        m_section_map = new HashMap<String,String> ();

        String config_line;
        
        // Parse a config file with global and named sections.
        // Example:
        //
        // GlobalParam=datavalue
        //
        // [SomInputModule]
        // SomInputAddress=127.0.0.1
        //
        // [DataTransferAgent]
        // AgeingPolicy=2
        // ControlChannelPort=$systemport
        //
        File fileInfo = new File (config_file_name);
        
        // Initial section name
        m_section_name = "";
                                
        if (fileInfo.canRead ())
        {
            // Read configuration details from the config file
            try
            {
                FileReader fr = new FileReader (config_file_name);
                BufferedReader in = new BufferedReader (fr);
                
                // Read all the lines and parse the data        
                while ((config_line = in.readLine ()) != null)
                {
                    parseConfigLine (config_line);
                }
                                
                fr.close ();
            }
            catch (Exception e)
            {
                throw new ConfigurationParsingException("Error: Config file access problem: " + e);
                //System.out.println ("Error: Config file access problem: " + e);
            }
        }
        else
        {
            throw new ConfigurationParsingException("Error: Config file does not exist: " + config_file_name);
        }
    }
    
    //
    // parseConfigLine():
    // Takes a text row from the config file and looks for section header
    // lines and key/value pairs.
    // All valid data is stored in the maps.
    //
    private void parseConfigLine (String line)
        throws ConfigurationParsingException
    {
        String key;
        String value;
        int separator_position = 0;

        // Parse either section header lines, enclosed in [] or
        // key/value pairs with "=" separator.        
        try
        {
            line.trim ();
            
            // Check for new sections in the file
            if (line.startsWith ("["))
            {
                // Create new section map
                m_section_map = new HashMap<String,String> ();

                // Allocate the new section header title
                m_section_name = line.substring (1, line.length() - 1);
            }
            else if (line.startsWith("#"))
            {
                // Line starts with a comment -> Ignored
            }
            else
            {
                // Look for "=" separator
                separator_position = line.indexOf ("=");
          
                // If a separator is found then extract and store the
                // key/value pair.
                if (separator_position > 0)
                {
                    key = line.substring (0, separator_position);
                    key.trim ();
                    
                    value = line.substring (separator_position + 1);
                    
                    // Check for variables in the value
                    value = expandValue (value);
                    
                    // Add key/value to the section map
                    m_section_map.put (key, value);
                          
                    //Add the section map to the config map
                    m_config_map.put (m_section_name, m_section_map);               
                }
            }
        }
        catch (Exception e)
        {
            throw new ConfigurationParsingException("Error: Invalid value in configuration: " +
                                                    line + " (" + e + ")");
        }
    }
    
    //
    // expandValue():
    // Converts $XXX variables with data from the environment which
    // must be passed via the java runtime as -D system properties
    //
    // e.g. java -Dusername=$USERNAME JmConfiguration filename
    //
    // allows $username to be used as a configuration value
    //
    private static String expandValue (String value)
    {
        String env_var;
        
        if (value.startsWith("$"))
        {
            env_var = System.getProperty (value.substring(1, value.length ()));
            
            if (env_var != null)
            {
               value = env_var;
            }
        }
        
        return (value);
    }
    
    //
    // getConfigValue():
    // Retrieve value from supplied section and key names and
    // returns null if not found.
    //
    public String getConfigValue (String section, String key)
    {
        Map<String,String> section_map;
        String value = null;
        
        if (m_config_map.size () > 0)
        {        
            // Return a configuration setting from the configuration.
            // Returns null if configuration value is not found.
            section_map = m_config_map.get (section);
         
            if (section_map.size () > 0)
            {
                if (section_map != null)
                {
                    value = section_map.get (key);
                }
            }
        }
        
        return (value);
    }
    
    public int getConfigValueInt (String section, String key) 
    										throws ConfigurationInvalidDataException, 
    											   ConfigurationNoDataAvailableException
    {
    	int value = 0;
    	String value_string;
    	   	
    	value_string = getConfigValue (section, key);
    	
    	if (value_string != null)
    	{
    		try
    		{
      			value = Integer.parseInt (value_string);
       		}
    		catch (Exception e)
    		{
    			throw new ConfigurationInvalidDataException ();
       		}
    	}
    	else
    	{
    		throw new ConfigurationNoDataAvailableException ();
    	}
    	    	   	    	
    	return (value);
    }
    //
    // getConfigSection():
    // Returns a map of all the key/value pairs stored for the supplied section.
    //
    public Map<String,String> getConfigSection (String section)
    {
        return (m_config_map.get(section));
    }
        
    //
    // dumpConfig():
    // Prints all stored configuration key/value pairs.
    //
    public void dumpConfig ()
    {
        // Print all the keys and values (debug function only)
        for (String section : m_config_map.keySet ())
        {
            for (String key : m_config_map.get (section).keySet ())
            {
                System.out.println (section + "." + 
                                    key + 
                                    " = '" + 
                                    m_config_map.get (section).get (key)
                                    + "'");
            }
        }
    }
}