package com.jm.wf.events.expressions.methods;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;

public class MethodNull extends MethodBase
{
    @Override
    public DataValue evaluate(Object object)
    {
        return DataValueFactory.createNullValue();
    }

}
