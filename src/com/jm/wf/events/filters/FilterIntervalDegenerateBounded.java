package com.jm.wf.events.filters;

import com.jm.wf.common.datavalues.DataValue;

public class FilterIntervalDegenerateBounded extends FilterIntervalBase
{

    public FilterIntervalDegenerateBounded(boolean   lowerClosed,
                                           boolean   upperClosed,
                                           DataValue lowerBound,
                                           DataValue upperBound)
    throws InvalidFilterIntervalException
    {
        super(lowerClosed, upperClosed, lowerBound, upperBound);
        
        if (!lowerClosed || !upperClosed ||
            (lowerBound == null) ||
            (upperBound == null) ||
            !lowerBound.equals(upperBound))
        {
            throw new InvalidFilterIntervalException("Degenerate bounded intervals must be closed and contain identical lower and upper boundary values");
        }
    }

    @Override
    public boolean matches(DataValue value)
    {
        return lowerBound.equals(value);
    }
    
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        
        if (lowerClosed)
            str.append("[");
        else
            str.append("(");
        
        if (lowerBound != null)
            str.append(lowerBound);
        
        str.append(",");
        
        if (upperBound != null)
            str.append(upperBound);
        
        if (upperClosed)
            str.append("]");
        else
            str.append(")");
        
        return str.toString();
    }

}
