package com.jm.wf.events.filters;

import com.jm.wf.common.datavalues.DataValue;

public class FilterIntervalNonDegenerateBounded extends FilterIntervalBase
{

    public FilterIntervalNonDegenerateBounded(boolean   lowerClosed,
                                              boolean   upperClosed,
                                              DataValue lowerBound,
                                              DataValue upperBound)
    throws InvalidFilterIntervalException
    {
        super(lowerClosed, upperClosed, lowerBound, upperBound);
        
        if (lowerBound == null || upperBound == null)
        {
            throw new InvalidFilterIntervalException("Both boundary values must be present in a non-degenerate bounded interval");
        }
    }

    @Override
    public boolean matches(DataValue value)
    {
        boolean result = true;

        if (lowerClosed)
        {
            result = result && (lowerBound.lessThan(value) || lowerBound.equals(value));
        }
        else
        {
            result = result && lowerBound.lessThan(value);
        }
        
        if (upperClosed)
        {
            result = result && (value.lessThan(upperBound) || value.equals(upperBound));
        }
        else
        {
            result = result && value.lessThan(upperBound);
        }
        
        return result;
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
