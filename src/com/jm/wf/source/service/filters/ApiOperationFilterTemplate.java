package com.jm.wf.source.service.filters;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueType;
import com.jm.wf.common.datavalues.InvalidDataTypeException;
import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.events.expressions.parsers.FieldValueExpressionParser;
import com.jm.wf.events.filters.Filter;
import com.jm.wf.events.filters.FilterFieldNotFoundException;
import com.jm.wf.events.filters.FilterTemplate;
import com.jm.wf.events.filters.FilterTemplateField;
import com.jm.wf.events.filters.InvalidFilterFormatException;

/**
 * ��Ӧ��OpenAPI api operation ����,
 * api operation������paraPath��queryPath,body��field
 * �������̣�ȱʡ��body field����ȡ��Ϣ
 * ����api.body.parent�� api�Ľ��������Ӧ�ľ���apiOperation
 * @author hongjian3
 *
 */
public class ApiOperationFilterTemplate implements FilterTemplate {
	
	private List<FilterTemplateField> fields = new ArrayList<FilterTemplateField>();
	
	
	/**
     * Given a string representing a filter template, this static method
     * creates a filter template object.
     */
    public static FilterTemplate create(String filterTemplateText)
    throws InvalidFilterFormatException
    {
        FilterTemplate template = null;

       // System.out.println("FilterTemplate "+filterTemplateText);;
        // Use the FilterTemplateParser to create the SURFilterTemplate object.
        
        try
        {
            StringReader strReader = new StringReader(filterTemplateText);
            Reader       reader    = new BufferedReader(strReader);
            
            FieldValueExpressionParser parser =
                new FieldValueExpressionParser(ApiOperationFilterTemplate.class,
                							   LoggerManager.getLogger(),
                                               reader);
        
            parser.ReInit(reader);
            
            template = parser.FilterTemplate(new ApiOperationFilterTemplate(), LoggerManager.getLogger());
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new InvalidFilterFormatException("Parse error in: " + filterTemplateText);
        }
        
        return template;
    }
	
	
	@Override
	public FilterTemplateField createFilterTemplateField(String fieldText) throws InvalidFilterFormatException {
         FilterTemplateField field = null;
        field = new ApiOperationParamTemplateField(fieldText);

    	return field;
	}

	@Override
	public void addTemplateField(String templateField) throws InvalidFilterFormatException {
		addTemplateField(ApiOperationParamTemplateField.create(templateField));

	}

	@Override
	public void addTemplateField(FilterTemplateField field) {
		fields.add(field);

	}

	@Override
	public List<FilterTemplateField> getFields() {
		return fields;
	}

	@Override
	public DataValueType getDataValueType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTruncationInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTruncationInterval(int truncationInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean matches(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataValue getValue(Object object) {
		//System.out.println("get value "+fields);
		DataValue value = null;
		for(FilterTemplateField field : fields) {
			try {
				value = field.getValue(object);
			} catch (InvalidDataTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FilterFieldNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if(value != null) {
				//System.out.println("filter template value "+value);
				//return value;
			//}
		}
		return value;
	}

	@Override
	public Filter createInstance(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filter createInstance(Object object, boolean allowNulls) {
		// TODO Auto-generated method stub
		return null;
	}

}
