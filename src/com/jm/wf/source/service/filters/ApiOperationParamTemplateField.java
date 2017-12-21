package com.jm.wf.source.service.filters;

import java.util.List;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueFactory;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.common.datavalues.InvalidDataTypeException;
import com.jm.wf.events.filters.FilterFieldNotFoundException;
import com.jm.wf.events.filters.FilterTemplateField;
import com.jm.wf.events.filters.FilterTemplateSubField;

public class ApiOperationParamTemplateField implements FilterTemplateField {

	private String fieldExpressionText = "";  
	private DataValueType fieldValueType = DataValueType.eString;
	
	public static FilterTemplateField create(String fieldText)
	{
		   //System.out.println("ApiOperationParamTemplateField create "+fieldText);
		   ApiOperationParamTemplateField field = null;
	        
	       field = new ApiOperationParamTemplateField(fieldText);
	        
	       return field;
	}
	protected ApiOperationParamTemplateField() {
		
	}
	protected ApiOperationParamTemplateField(String fieldText) {
		// System.out.println("ApiOperationParamTemplateField  "+fieldText);
		fieldExpressionText = fieldText;
	}
	@Override
	public FilterTemplateSubField createFilterTemplateSubField(String strSubFieldName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addSubField(String subFieldText) {
		// TODO Auto-generated method stub

	}

	@Override
	public void instantiateField(boolean inst) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInstantiated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void negateField(boolean neg) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNegated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFormatString(String fmtStr) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFormatString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataValueType getFieldType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSubField(FilterTemplateSubField subField) {

	}

	@Override
	public DataValue getValue(Object object) throws InvalidDataTypeException, FilterFieldNotFoundException {
		DataValue fieldValue = DataValueFactory.createNullValue();
		//System.out.println("fieldName get  "+fieldExpressionText);
		if(fieldExpressionText != null && fieldExpressionText.startsWith("api.body")) {
			String fieldName = fieldExpressionText.split("\\.")[2];
			fieldValue = ApiBodyParam.getFieldValue(fieldName, object);
		}else if (fieldExpressionText != null && !fieldExpressionText.contains("\\.")){
			fieldValue = DataValueFactory.createDataValue(fieldExpressionText, DataValueType.eString);
		} else {
			// more complex expressions
		}
		
		return fieldValue;
	}

	@Override
	public List<FilterTemplateSubField> getSubFields() {
		// TODO Auto-generated method stub
		return null;
	}

}
