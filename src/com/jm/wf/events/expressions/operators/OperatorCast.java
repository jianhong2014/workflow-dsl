package com.jm.wf.events.expressions.operators;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;

public class OperatorCast extends OperatorBase
{
    private DataValueType castType = DataValueType.eUnknown;
    
    
    public OperatorCast()
    {
        setOperatorName("()");
    }

    @Override
	public DataValue evaluate(Object object)
	{
		DataValue rhsValue = getRhs().evaluate(object);

		return DataValueFactory.createDataValue(rhsValue,
                                                castType);
	}

    public DataValueType getCastType()
    {
        return castType;
    }

    public void setCastType(DataValueType castType)
    {
        this.castType = castType;
    }

    public void setCastType(String castTypeName)
    {
        this.castType = DataValueType.getDataValueType(castTypeName);
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();

        str.append("(").append(castType.getName()).append(") ").append(getRhs());
        
        return str.toString();
    }
}
