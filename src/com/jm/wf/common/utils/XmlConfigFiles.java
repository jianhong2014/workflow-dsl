package com.jm.wf.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jm.wf.common.logging.LoggerManager;


/**
 *  Takes a directory and reads all the files in said directory into an array 
 * 
 *
 */
public class XmlConfigFiles
{
    private String mDirString = null;
    private String[] mFileNameArray = null;
    private long[] mFileModArray = null;
    private List<String> configFileList = null;

    
    /**
     *  
     * @param dirString The Directory path
     * @throws Exception if the string does not point to a directory on the filesystem
     */
    public XmlConfigFiles( String dirString ) throws Exception
    {
        File cfgDir = new File( dirString );
        
        if( !cfgDir.isDirectory() )
        {
            throw new Exception( dirString + " is not a directory" );
        }
        mDirString = dirString;
        if( scanConfigFiles(configFileList) == 0 )
        {
            LoggerManager.getLogger().warn( dirString + " contains no config files" );
        }
    }
    
    /**
     *  
     * @param dirString The Directory path
     * @param fileList  specific files under a directory
     * @throws Exception if the string does not point to a directory on the filesystem
     */
    public XmlConfigFiles( String dirString, List<String> fileList ) throws Exception
    {
        File cfgDir = new File( dirString );

        this.configFileList = fileList;
        
        if( !cfgDir.isDirectory() )
        {
            throw new Exception( dirString + " is not a directory" );
        }
        mDirString = dirString;
        if( scanConfigFiles(configFileList) == 0 )
        {
            LoggerManager.getLogger().warn( dirString + " contains no config files" );
        }
    }
    
    public String getDirString()
    {
        return mDirString;
    }

    public String[] getFileNameArray()
    {
        return mFileNameArray;
    }
    
    public long[] getFileModArray()
    {
        return mFileModArray;
    }
    
    /**
     * Generates a list of files in the given directory.
     * @return the number of files scanned
     */
    private int scanConfigFiles(List<String> fileList) 
    {
        int Index = 0;
        SortedSet<File> sortedSet = null;
        SortedSet<File> filteredSet = new TreeSet<File>();

        if( mDirString != null )
        {
            File cfgDir = new File( mDirString );
            try
            {
                // Generate a list of all files in the XML config directory
                for( File cfgFile : cfgDir.listFiles() )
                {
                    // only want to bother about kosher files that are not hidden and are readable
                    if( cfgFile.isFile() && (!cfgFile.isHidden()) && cfgFile.canRead() )
                    {
                        if( sortedSet == null )
                        {
                            sortedSet = new TreeSet<File>();
                        }
                        
                        sortedSet.add( cfgFile );
                    }
                }
                
                /*
                 * Note that sortedSet will be null when the associated directory is
                 * empty - an acceptable position for the Enricher.
                 */
                if (fileList != null && !fileList.isEmpty())
                {             	
	                for (String fileName : fileList)
	                {
	                    String trimName = fileName.replace("\"", "");
	                	boolean found = false;
	                	for (File file : sortedSet)
	                	{
	                		if (file.getName().equals(trimName))
	                		{
	                			filteredSet.add(file);
	                			found = true;
	                		}
	                	}
	                	
	                    if (!found)
	                	{
	                		LoggerManager.getLogger().warn("File " + fileName + " in directory" + 
	                				cfgDir.getAbsolutePath() + " missing or not readable");
	                	}
	                }
                }
                else if (sortedSet != null)
                {
                	filteredSet.addAll(sortedSet);
                }
                
                if( filteredSet != null && !filteredSet.isEmpty())
                {
                    mFileNameArray = new String[filteredSet.size()];
                    mFileModArray = new long[filteredSet.size()];
                    Iterator<File> FileIter = filteredSet.iterator();
                    while( FileIter.hasNext() )
                    {
                        File tmpFile = FileIter.next();
                        mFileModArray[Index] = tmpFile.lastModified(); 
                        mFileNameArray[Index] = tmpFile.getName();
                        Index++;
                    }
                }
            }
            catch( Exception e )
            {
                LoggerManager.getLogger().error( "scanConfigFiles: " + e );
            }
        }
        return Index;
    }
    
    /**
     * Determines if the config files have changed.
     * This method checks if the directroy path is the same, then if the number of files is the same,
     * then if the filenames are the same and finally if the last modified dates are the same.
     * @return true is the objects are equal, false otherwise
     */
    public boolean equals( Object other )
    {
        boolean retVal = true;
        XmlConfigFiles tmpConfigFiles = (XmlConfigFiles)other;

        // are the directories the same?
        if( !tmpConfigFiles.getDirString().equals( this.mDirString ) )
        {
            retVal = false;
        }
        else
        {
            // is the number of files in the directories the same?
            String[] tmpFileNameArray = tmpConfigFiles.getFileNameArray();
            long[] tmpFileModArray = tmpConfigFiles.getFileModArray();
            if( tmpFileNameArray.length != this.mFileNameArray.length )
            {
                retVal = false;
            }
            else
            {
                // are the filenames and the lastModified values in the directories the same
                for( int Index = 0; Index < tmpFileNameArray.length; Index++ )
                {
                    if( ( !tmpFileNameArray[Index].equals( this.mFileNameArray[Index] ) ) ||
                        ( tmpFileModArray[Index] != this.mFileModArray[Index] ) )
                    {
                        retVal = false;
                        break;
                    }
                }
            }
        }
        return retVal;
    }
    
    public static List<String> reloadConfigFileList(String cfgFile, String beanName)
    {
    	List<String> confileList = new ArrayList<String>();
    	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try
		{
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    File f = new File(cfgFile);
		
		    Document doc = db.parse(f);
		    NodeList beanList = doc.getElementsByTagName("bean");
		    
		    //go through all beans
		    for (int i=0; i<beanList.getLength(); i++)
		    {
		    	Element element = (Element)beanList.item(i);
		    	String beanname = element.getAttribute("id");
		    	
		    	//find the target bean 
		    	if (beanname.equalsIgnoreCase(beanName))
		    	{
 		    		NodeList propertyList = element.getElementsByTagName("property");
 		    		//go through all property
		    		for (int j=0; j<propertyList.getLength(); j++)
		    		{
		    			Element pelement = (Element)propertyList.item(j);
		    			//find the target property
		    			if (pelement.getAttribute("name").equalsIgnoreCase("configFileList"))
		    			{
		    				Element lelement = (Element)pelement.getElementsByTagName("list").item(0);
		    				
		    				NodeList valueList = lelement.getElementsByTagName("value");
		    				//go through all filename define here
		    				for (int k=0; k<valueList.getLength(); k++)
		    				{
				    			Element fileElement = (Element)valueList.item(k);
				    			String fileName = fileElement.getTextContent();
				    			confileList.add(fileName);
		    				}
		    				break;
		    			}
		    		}
		    		break;
		    	}
		    }
		}
		catch(ParserConfigurationException e)
		{
			LoggerManager.getLogger().error( "ParserConfigurationException in reloadConfigFileList : " + e );
		}
		catch(SAXException e)
		{
			LoggerManager.getLogger().error( "SAXException in reloadConfigFileList: " + e );
		}
		catch(IOException e)
		{
			LoggerManager.getLogger().error( "IOException in reloadConfigFileList: " + e );
		}

		LoggerManager.getLogger().info( "reloadConfigFileList " +  beanName + " from " + cfgFile);
    	return confileList;
    }
}


