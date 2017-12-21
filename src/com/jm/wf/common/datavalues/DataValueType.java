package com.jm.wf.common.datavalues;


public enum DataValueType
{
    eUnknown      ("unknown"),
    eInteger      ("integer"),
    eReal         ("real"),
    eBoolean      ("boolean"),
    eString       ("string"),
    eTimestamp    ("timestamp"),
    eDuration     ("duration"),
    eIPv4Address  ("ipv4address"),
    eOctetString  ("octetstring"),
    eNoPadTelNo   ("NopaddingTelNo"),
    eBSID         ("bsid"),
    eESN          ("esn"),
    eAdHocService ("adhocservice"),
    eStatusCode   ("statuscode"),
    eNull         ("null"),
    eList         ("list"),
    eSet          ("set"),
    eMap          ("map"),
    ePointCode    ("pointcode");
    
    private String name = null;
    
    private DataValueType(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public static DataValueType getDataValueType(String name)
    {
        DataValueType type = eUnknown;
        
        for (DataValueType t : DataValueType.values())
        {
            if (t.getName().equalsIgnoreCase(name))
            {
                type = t;
                
                break;
            }
        }
        
        return type;
    }
}
