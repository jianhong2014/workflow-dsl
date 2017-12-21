package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueCollection;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;

/**
 * This Method expects four parameters for evaluation:
 * 
 * 	1. A measured object ID.
 * 	2. A monitored measured object ID.
 * 	3. A set of measured object IDs for an originator.
 * 	4. A set of measured object IDs for a destination.
 * 
 *  The basic principle is to match the measured or monitored IDs to the 
 *  orig and dest measured object sets. True is returned when
 *  both measured == orig and monitored == dest or vice versa.
 *  
 *  A special case is when the measured and monitored IDs are the same. In this
 *  case the measured ID must match either the orig or dest sets.
 */

public class MethodMoMatch extends MethodBase
{
	@Override
    public DataValue evaluate(Object object)
    {
		boolean result = false;
        
        DataValue measId = getParameters().get(0).evaluate(object);
        DataValue monId  = getParameters().get(1).evaluate(object);
        DataValueCollection origMoIdSet = (DataValueCollection)getParameters().get(2).evaluate(object);
        DataValueCollection destMoIdSet = (DataValueCollection)getParameters().get(3).evaluate(object);

        if (measId != null && monId != null && origMoIdSet != null && destMoIdSet != null)
        {        	
	        if ( ((measId.toInteger() == monId.toInteger()) &&
	        		(origMoIdSet.contains(measId) || destMoIdSet.contains(measId))) ||
	
	        		(origMoIdSet.contains(monId) && destMoIdSet.contains(measId)) ||
	
	        		(origMoIdSet.contains(measId) && destMoIdSet.contains(monId)))
	        {
	        	result = true;
	        }
        }

        return DataValueFactory.createDataValue(result ? 1 : 0,
                                                DataValueType.eBoolean);
    }
}

/*** END OF FILE ***/
