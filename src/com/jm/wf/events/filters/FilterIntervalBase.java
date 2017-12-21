package com.jm.wf.events.filters;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;


import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.events.expressions.parsers.FieldValueExpressionParser;

public abstract class FilterIntervalBase implements FilterInterval
{
    protected DataValue lowerBound  = null;
    protected boolean   lowerClosed = true;

    protected DataValue upperBound  = null;
    protected boolean   upperClosed = true;

    /**
     * <h2>FilterIntervalBase.create()</h2>
     * <p>
     * A <b>filter interval</b> is a member of a filter template sub-field group that defines a range of
     * values that are to be matched when processing events.  Each filter interval can take one of
     * three forms:
     * <ul>
     * <li>A discrete value that is matched directly</li>
     * <li>A range using a-b shorthand interval notation where a or b may be missing to indicate an unbounded
     * interval</li>
     * <li>An bounded or unbounded interval using mathematical interval notation</li>
     * </ul>
     * </p><p>
     * Where shorthand interval notation is being used, the intervals may be defined as described in
     * the following table:</p><p>
     * <table border="1" cellpadding="5">
     * <thead>
     * <tr><td align="center">Notation</td><td align="center">Meaning</td><td>Description</td></tr>
     * </thead>
     * <tbody>
     * <tr><td align="center">a-b</td><td align="center">{x | a <= x <= b}</td><td>Bounded non-degenerate interval: [a, b]</td></tr>
     * <tr><td align="center">a-</td><td align="center">{x | x >= a}</td><td>Unbounded interval: [a, )</td></tr>
     * <tr><td align="center">-b</td><td align="center">{x | x <= b}</td><td>Unbounded interval: (, b]</td></tr>
     * <tr><td align="center">a</td><td align="center">{x | x = a}</td><td>Bounded degenerate interval: [a, a]</td></tr>
     * </tbody>
     * </table> 
     * </p>
     * Where mathematical interval notation is being used, the intervals may be defined as described in
     * the following table:</p><p>
     * <table border="1" cellpadding="5">
     * <thead>
     * <tr><td align="center">Notation</td><td align="center">Meaning</td><td>Description</td></tr>
     * </thead>
     * <tbody>
     * <tr><td align="center">(a, b)</td><td align="center">{x | a < x < b}</td><td>Bounded non-degenerate open interval</td></tr>
     * <tr><td align="center">[a, b]</td><td align="center">{x | a <= x <= b}</td><td>Bounded non-degenerate closed interval</td></tr>
     * <tr><td align="center">(a, b]</td><td align="center">{x | a < x <= b}</td><td>Bounded non-degenerate half-closed interval</td></tr>
     * <tr><td align="center">[a, b)</td><td align="center">{x | a <= x < b}</td><td>Bounded non-degenerate half-closed interval</td></tr>
     * <tr><td align="center">(a, )</td><td align="center">{x | x > a}</td><td>Unbounded open interval</td></tr>
     * <tr><td align="center">[a, )</td><td align="center">{x | x >= a}</td><td>Unbounded closed interval</td></tr>
     * <tr><td align="center">( , b)</td><td align="center">{x | x < b}</td><td>Unbounded open interval</td></tr>
     * <tr><td align="center">( , b]</td><td align="center">{x | x <= b}</td><td>Unbounded closed interval</td></tr>
     * <tr><td align="center">( , )</td><td align="center">{x | -&#8734; < x < &#8734;}</td><td>Unbounded open interval</td></tr>
     * <tr><td align="center">[a, a]</td><td align="center">{x | x = a}</td><td>Bounded degenerate closed interval</td></tr>
     * </tbody>
     * </table> 
     * </p>
     * @param strInterval String containing the text that is to be converted to a FilterExpression
     * @param dataType The DataValueType to be used in creating the boundary DataValue objects
     * @return A fully constructed FilterInterval object
     */
    public static FilterInterval create(String        strInterval,
                                        DataValueType dataType)
    throws InvalidFilterIntervalException
    {
        FilterInterval interval = null;

        try
        {
            StringReader strReader = new StringReader(strInterval);
            Reader       reader    = new BufferedReader(strReader);
            
            FieldValueExpressionParser parser = new FieldValueExpressionParser(null,
            																   LoggerManager.getLogger(),
                                                                               reader);

            parser.ReInit(reader);
            
            interval = parser.FilterInterval(dataType);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            throw new InvalidFilterIntervalException(e);
        }
        
        return interval;
    }
    
    public static List<FilterInterval> createList(String        strInterval,
                                                  DataValueType dataType)
    throws InvalidFilterIntervalException
    {
        List<FilterInterval> intervals = null;

        try
        {
            StringReader strReader = new StringReader(strInterval);
            Reader       reader    = new BufferedReader(strReader);
            
            FieldValueExpressionParser parser = new FieldValueExpressionParser(null,
            																   LoggerManager.getLogger(),
                                                                               reader);

            parser.ReInit(reader);
            
            intervals = parser.FilterIntervals(dataType);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            throw new InvalidFilterIntervalException(e);
        }
        
        return intervals;
    }
    
    protected FilterIntervalBase(boolean   lowerClosed,
                                 boolean   upperClosed,
                                 DataValue lowerBound,
                                 DataValue upperBound)
    {
        this.lowerClosed = lowerClosed;
        this.upperClosed = upperClosed;
        this.lowerBound  = lowerBound;
        this.upperBound  = upperBound;
    }
    
    protected FilterIntervalBase(DataValue lowerBound,
                                 DataValue upperBound)
    {
        this(true, true, lowerBound, upperBound);
    }

    public void updateFieldType(DataValueType fieldType)
    {
        lowerBound = DataValueFactory.createDataValue(lowerBound,
                                                      fieldType);

        upperBound = DataValueFactory.createDataValue(upperBound,
                                                      fieldType);
    }
    
    @Override
    public abstract boolean matches(DataValue value);
    
    public DataValue getLowerBound()
    {
        return lowerBound;
    }

    public void setLowerBound(DataValue lowerBound)
    {
        this.lowerBound = lowerBound;
    }

    public DataValue getUpperBound()
    {
        return upperBound;
    }

    public void setUpperBound(DataValue upperBound)
    {
        this.upperBound = upperBound;
    }

}
