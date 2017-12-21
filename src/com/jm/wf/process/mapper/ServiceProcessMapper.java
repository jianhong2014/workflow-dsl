package com.jm.wf.process.mapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.xml.sax.ErrorHandler;

import com.jm.wf.common.logging.LoggerManager;



public class ServiceProcessMapper {

	static private ServiceProcessMapper mapper  =  new ServiceProcessMapper();
	private List<ProcessMapping> processMappings = new ArrayList<ProcessMapping>();
	
	
	private ServiceProcessMapper() {
		
	}
	public static ServiceProcessMapper getServiceProcessMapper() {
	      return mapper;	
	}
	public void addProcessMapping(ProcessMapping processMapping) {
		this.processMappings.add(processMapping);
	}
	
	public void init(String mapperFile) {
		  // System.out.println(mapperFile); 
		  this.readMapping(mapperFile);
		
	}
	private boolean readMapping(String mapperFile)
	{
		boolean mappingRead = false;
		
		String[] params = new String[]{"java.lang.String"};
		
		File xmlInput = new File(mapperFile);
		if( !xmlInput.exists() )
        {
			System.out.println("not found xml file");
			LoggerManager.getLogger().error( xmlInput.getName() + " does not exist in directory\n" + xmlInput.getParent() );
			return mappingRead;
        }
		
		Digester digester = new Digester();
		XMLErrorHandler xmlErrorHandler = new XMLErrorHandler();
        
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.setErrorHandler((ErrorHandler) xmlErrorHandler);
        digester.push( this);  

		digester.addObjectCreate       ( "*/mapper", ProcessMapping.class );
		digester.addSetProperties      ( "*/mapper", "process", "process" );
		digester.addObjectCreate       ( "*/mapper/srcApi", ApiMapping.class);
		digester.addSetProperties      ( "*/mapper/srcApi", "id", "apiId" );
		
		
		digester.addObjectCreate       ( "*/mapper/srcApi/task", TaskMapping.class);
		digester.addSetProperties      ( "*/mapper/srcApi/task", "name", "name" );
		
		//1.1 add fields under task
		digester.addObjectCreate       ( "*/mapper/srcApi/task/fields/field", Field.class);
		digester.addSetProperties      ( "*/mapper/srcApi/task/fields/field", "name", "name" );
		digester.addCallMethod		   ( "*/mapper/srcApi/task/fields/field/value", "addValue", 0, params ); 	
		digester.addSetNext			   ( "*/mapper/srcApi/task/fields/field", "addField" );
		//1.2 add decisions under task fields
		digester.addObjectCreate       ( "*/task/fields/decisions", Decisions.class);
		digester.addSetProperties      ( "*/task/fields/decisions", "switch", "decName" );
		digester.addObjectCreate       ( "*/task/fields/decisions/decision", Decision.class);
		digester.addSetProperties      ( "*/task/fields/decisions/decision", "case", "strCaseVal" );
		
		digester.addObjectCreate       ( "*/task/fields/decisions/decision/field", Field.class);
		digester.addSetProperties      ( "*/task/fields/decisions/decision/field", "name", "name" );
		digester.addCallMethod		   ( "*/task/fields/decisions/decision/field/value", "addValue", 0, params ); 	
		digester.addSetNext			   ( "*/task/fields/decisions/decision/field", "addField" );
		digester.addSetNext 		   ( "*/task/fields/decisions/decision/", "addCase");
		digester.addSetNext 		   ( "*/task/fields/decisions/", "addDecSwitch");
		digester.addSetNext 		   ( "*/mapper/srcApi/task", "addTaskMapping");
		
		//2 decisions under srcApi
		digester.addObjectCreate       ( "*/srcApi/decisions", Decisions.class);
		digester.addSetProperties      ( "*/srcApi/decisions", "switch", "decName" );
		digester.addObjectCreate       ( "*/srcApi/decisions/decision", Decision.class);
		digester.addSetProperties      ( "*/srcApi/decisions/decision", "case", "strCaseVal" );
		
		//2.1 task under decision case
		digester.addObjectCreate       ( "*/decisions/decision/task", TaskMapping.class);
		digester.addSetProperties      ( "*/decisions/decision/task", "name", "name" );
		digester.addObjectCreate       ( "*/decisions/decision/task/fields/field", Field.class);
		digester.addSetProperties      ( "*/decisions/decision/task/fields/field", "name", "name" );
		digester.addCallMethod		   ( "*/decisions/decision/task/fields/field/value", "addValue", 0, params );
		digester.addSetNext			   ( "*/decisions/decision/task/fields/field", "addField" );
		digester.addSetNext 		   ( "*/decisions/decision/task", "addTaskMapping");
		digester.addSetNext 		   ( "*/srcApi/decisions/decision/", "addCase");
		digester.addSetNext 		   ( "*/srcApi/decisions/", "addDecSwitch");
		digester.addSetNext 		   ( "*/mapper/srcApi", "addApiMapping");

		digester.addSetNext 		   ( "*/mapper", "addProcessMapping");
		
		try
		{
			digester.parse(xmlInput);
			LoggerManager.getLogger().info("process mapping "+processMappings);
			//System.out.println("process mapping "+processMappings);
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LoggerManager.getLogger().error("Error parsing Mapping XML");
			mappingRead = false;
		}
		
		//logger = oldLogger;
		return mappingRead;
	}
	
	public Map<String,Map<String,Object>> map(String apiId, Object request) {
		 
		return processMappings.get(0).getApiMapping(apiId, request);
	}
	
}
