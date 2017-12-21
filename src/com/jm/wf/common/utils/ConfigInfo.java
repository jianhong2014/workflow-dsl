package com.jm.wf.common.utils;

public final class ConfigInfo
{
    private String moduleName    = "";
    private String configFile    = "";
    private String configSection = "";

    public ConfigInfo()
    {
        
    }
    
    public ConfigInfo(String module, String file, String section)
    {
        moduleName    = module;
        configFile    = file;
        configSection = section;
    }
    
    public String getModuleName()
    {
        return moduleName;
    }
    
    public String getConfigFile()
    {
        return configFile;
    }
    
    public String getConfigSection()
    {
        return configSection;
    }
}
