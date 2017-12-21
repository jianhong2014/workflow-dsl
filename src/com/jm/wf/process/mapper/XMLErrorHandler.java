package com.jm.wf.process.mapper;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLErrorHandler implements ErrorHandler
{

    @Override
    public void error( SAXParseException arg0 ) throws SAXException
    {
        throw new SAXException( arg0 );
    }

    @Override
    public void fatalError( SAXParseException arg0 ) throws SAXException
    {
        throw new SAXException( arg0 );
    }

    @Override
    public void warning( SAXParseException arg0 ) throws SAXException
    {
        throw new SAXException( arg0 );
    }

}
