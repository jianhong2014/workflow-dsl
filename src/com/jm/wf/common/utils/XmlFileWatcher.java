package com.jm.wf.common.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.io.StringWriter;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationErrorEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.configuration.event.ConfigurationErrorListener;

import com.jm.wf.common.logging.LoggerManager;

/*********************
 *
 * XmlFileWatcher xmlWatcher;
 * String file;
 * String key;
 * XmlFileWatcher.ChangeListener changeListener;
 * xmlWatcher.addFileKey(file,key);
 * xmlWatcher.setChangeListener(changeListener);  
 * xmlWatcher.startWatcher();
 * xmlWatcher.stopWatcher();
 *
 *******************/

public class XmlFileWatcher implements Runnable{

  private int interval=2;
  private Map<String,FileProp> fileProps=new HashMap<String,FileProp> ();
  private boolean stopRequested=false;
  private Thread  thread=null;
  private ChangeListener changeListener=null;

  // the listener interface for client
  public interface ChangeListener
  {
    public void configurationChanged(ChangeEvent event);
  }

  /**
   * Add file and key(XPath String of XML node) to watch the changes
   * 
   * @param file: The XML file
   * @param key: The XPath for the interested node. 
   * E.g. below the XML file, the key " /bean[@id="fwk_server1_fwk1"]//ref/@bean " 
   * will return the list of the plugin components
   *  iep_server1_fwk1
   *  es_server1_fwk1
   *  aof_server1_fwk1
   *
   *  <bean id="fwk_server1_fwk1"
   *        class="com.jd.wf.frameworksupport.HouseKeeping"
   *        scope="prototype">
   *    <property name="name" value="fwk_server1_fwk1" />
   *    <property name="eventBroker" ref="ebk_server1_fwk1" />
   *    <property name="houseKeepingPublisher" ref="ebk_server1_fwk1" />
   *    <property name="eventFactory" ref="evf_common" />
   *    <property name="heartBeatInterval" value="10" />
   *    <property name="loopDelay" value="10" />
   *    <property name="pluginComponents">
   *      <list>
   *        <ref bean="iep_server1_fwk1" />
   *        <ref bean="es_server1_fwk1" />
   *        <ref bean="aof_server1_fwk1" />
   *      </list>
   *    </property>
   * </bean>
   */
  public void addFileKey(String file,String key)
  {
    LoggerManager.getLogger().info("Watch out! file:"+file+","+"key:"+key);

    stopWatcher();

    if(fileProps.containsKey(file))
    {
      fileProps.get(file).keys.add(key);
    }
    else
    {
      FileProp fileProp=new FileProp(file);
      fileProp.keys.add(key);
      fileProps.put(file,fileProp);
    }
  }

  void clearFileKeys()
  {
    stopWatcher();
    fileProps.clear();
  }

  public void setChangeListener(ChangeListener changeListener)
  {
    stopWatcher();
    this.changeListener=changeListener;
  }

  public void unsetChangeListener()
  {
    stopWatcher();
    this.changeListener=null;
  }

  public void startWatcher()
  {
    stopWatcher();
    init();

    thread=new Thread(this);
    thread.start();

  }

  public void stopWatcher()
  {
    stopRequested=true;

    if(thread!=null)
    {
      thread.interrupt();
      try
      {
        thread.join();
      }
      catch (InterruptedException e)
      {
      }
      thread=null;
    }

  }

  public void reset()
  {
    stopWatcher();
    clearFileKeys();
    unsetChangeListener();
  }

  public class ChangeEvent
  {
    private String file;
    private String key;

    public ChangeEvent(String file,String key)
    {
      this.file=file;
      this.key=key;
    }

    public String getFile()
    {
      return this.file;
    } 

    public String getKey()
    {
      return this.key;
    }

  }

  public int getInterval(int interval)
  {
    return this.interval;
  }

  public void setInterval(int interval)
  {
    this.interval=interval;
  }

  private void init()
  {
    stopRequested=false;
    thread=null;
  }

  private class MyChangeListener implements ChangeListener
  {
    public void configurationChanged(ChangeEvent event)
    {
      LoggerManager.getLogger().info("changeEvent "+"file:"+event.getFile()+","+"key:"+event.getKey());
    }
  }

  public static void main(String[] args) {
    LoggerManager.getLogger().info("args:" + args[0] + " " + args[1]);

    XmlFileWatcher watcher=new XmlFileWatcher();
    watcher.testRun(args[0],args[1]);
  }

  public void testRun(String file,String key)
  {
    ChangeListener changeListener=new MyChangeListener();
    addFileKey(file,key);
    setChangeListener(changeListener);  

    startWatcher();

    try
    {
      Thread.sleep(10*interval*1000);
    }
    catch (Exception cex)
    {
    }

    stopWatcher();
  }

  public void run()
  {
    boolean hasError=false;

    do
    {
      hasError=false;

      try
      {

        StringWriter nodeWriter = null;
        List nodeList = null;

        FileProp fileProp=null;

        // store the node value for the 1st start
        for (String file:fileProps.keySet())
        {
          fileProp=fileProps.get(file);
          for(String key:fileProp.keys)
          {
            nodeWriter= new StringWriter();
            nodeList = fileProp.config.configurationsAt(key);

            for (Iterator iter = nodeList.iterator(); iter.hasNext();) {
              printNode( ( (SubnodeConfiguration) iter.next() ).getRootNode() , nodeWriter);
            }
            nodeWriter.flush();
            fileProp.oldNodeValue.put( key,nodeWriter.toString() );
            nodeWriter.close();
          }
        }

        while( !stopRequested )
        {

          for (String file:fileProps.keySet())
          {
            fileProp=fileProps.get(file);

            // check the time change
            fileProp.config.configurationsAt(fileProp.keys.iterator().next());
            if(fileProp.isChanged)
            {
              for(String key:fileProp.keys)
              {
                // get the nodes associated with this key
                nodeList = fileProp.config.configurationsAt(key);

                nodeWriter = new StringWriter();
                for (Iterator iter = nodeList.iterator(); iter.hasNext();) {
                  // get the value for the nodes
                  printNode( ( (SubnodeConfiguration) iter.next() ).getRootNode() , nodeWriter );
                }


                nodeWriter.flush();
                // if different with the old value
                if(!nodeWriter.toString().equals( fileProp.oldNodeValue.get(key) ) )
                {
                  LoggerManager.getLogger().info("file:" + file + " " + "key:" + key);
                  LoggerManager.getLogger().debug("oldValue:\n"+fileProp.oldNodeValue.get(key));

                  fileProp.oldNodeValue.remove( key );
                  fileProp.oldNodeValue.put( key,nodeWriter.toString() );

                  ChangeEvent changeEvent=new ChangeEvent(fileProp.file,key);
                  if(changeListener!=null)
                  {
                    // fire the change event
                    changeListener.configurationChanged(changeEvent);
                  }
                  LoggerManager.getLogger().debug("newValue:\n"+nodeWriter.toString());
                }
                nodeWriter.close();

              }
              fileProp.isChanged=false;
            }
          }
          Thread.sleep(interval*1000);
        }
      }
      catch(Exception cex)
      {
        hasError=true;
        // something went wrong, e.g. the file was not found
      }

      if(hasError)
      {
        try
        {
          Thread.sleep(interval*1000);
        }
        catch(Exception cex)
        {
          //Nothing to do
        }
      }

    }while(hasError);
  }

  private void printNode(ConfigurationNode node,StringWriter nodeValue)
  {
    if (node.isAttribute()) {
      nodeValue.write("Attr->" + node.getName() + ":" + node.getValue() + "\n");
    }
    else
    {
      nodeValue.write("Element->" + node.getName() + ":" + node.getValue() + "\n");

      List attrNodeList = node.getAttributes();
      for (Iterator iter = attrNodeList.iterator(); iter.hasNext();) {
        printNode( (ConfigurationNode) iter.next(),nodeValue);
      }

      List childNodeList = node.getChildren();
      for (Iterator iter = childNodeList.iterator(); iter.hasNext();) {
        printNode( (ConfigurationNode) iter.next(),nodeValue);
      }

    }
  }

  private class FileProp
  {

    String file=null;
    boolean     isChanged=true;

    XMLConfiguration config=null;
    ConfigurationLogListener listener=null;

    Set<String> keys=new HashSet<String>();
    Map<String ,String > oldNodeValue=new HashMap<String ,String >();//key's old value

    public FileProp(String file)
    {
      this.file=file;

      try
      {
        config = new XMLConfiguration(file); 

        listener=new ConfigurationLogListener();

        config.addConfigurationListener(listener);
        config.addErrorListener((ConfigurationErrorListener) listener);
        config.setReloadingStrategy(new FileChangedReloadingStrategy());
        config.setExpressionEngine(new XPathExpressionEngine());

      }
      catch(ConfigurationException e) 
      {
        LoggerManager.getLogger().error("ConfigurationException");
      }
    }


    private class ConfigurationLogListener implements ConfigurationListener, ConfigurationErrorListener
    {
      public void configurationChanged(ConfigurationEvent event)
      {
        isChanged=true;
      }

      public void configurationError(ConfigurationErrorEvent event)
      {
        // Log the standard properties of the configuration event
        // configurationChanged(event);
        // Now log the exception
        event.getCause().printStackTrace();
      }
    }

  }
}
