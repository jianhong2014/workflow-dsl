package com.jm.wf.events.filters;

import com.jm.wf.common.datavalues.DataValue;

public class FilterIntervalUnbounded extends FilterIntervalBase
{

    public FilterIntervalUnbounded(boolean   lowerClosed,
                                   boolean   upperClosed,
                                   DataValue lowerBound,
                                   DataValue upperBound)
    throws InvalidFilterIntervalException
    {
        super(lowerClosed, upperClosed, lowerBound, upperBound);
        
        if ((lowerClosed && lowerBound == null) ||
            (upperClosed && upperBound == null))
        {
            throw new InvalidFilterIntervalException("Missing boundary must be accompanied by an open interval in an unbounded interval");
        }
    }


    @Override
    public boolean matches(DataValue value)
    {
        boolean result = true;

        if (lowerClosed)
        {
            result = result && ((lowerBound == null) ||
                                 lowerBound.lessThan(value) ||
                                 lowerBound.equals(value));
        }
        else
        {
            result = result && ((lowerBound == null) ||
                                 lowerBound.lessThan(value));
        }
        
        if (upperClosed)
        {
            result = result && ((upperBound == null) ||
                                 value.lessThan(upperBound) ||
                                 value.equals(upperBound));
        }
        else
        {
            result = result && ((upperBound == null) ||
                                 value.lessThan(upperBound));
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
