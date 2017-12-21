package com.jm.wf.events.expressions;

public class InvalidExpressionFormatException extends Exception
{
    static final long serialVersionUID = 0L;
    
    public InvalidExpressionFormatException()
    {
        super();
    }

    public InvalidExpressionFormatException(String descr)
    {
        super(descr);
    }

    public InvalidExpressionFormatException(Exception e)
    {
        super(e);
    }
}
