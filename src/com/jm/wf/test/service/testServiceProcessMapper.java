package com.jm.wf.test.service;

import com.jm.wf.common.utils.WfRflectionUtils;
import com.jm.wf.process.mapper.ServiceProcessMapper;

public class testServiceProcessMapper {
    public static ServiceProcessMapper mapper = ServiceProcessMapper.getServiceProcessMapper();
	
	public static void main(String[] args) {
		mapper.init("D:\\Project\\JavaCC\\WorkFlowJavaCC\\src\\com\\jd\\wf\\test\\service\\service-mapper.xml");
		System.out.println(mapper.map("commandApi", new Request()));

	}
	
	static public void testRlection() {
		try {
			System.out.println(WfRflectionUtils.getFieldAndType("taskId", new Request())[0]);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static public void switchCase() {
		switch(getVal()) {
		case 1:
			System.out.println("yes, youare right ");
			break;
		case 2:
			break;

		}
	}
	static public int getVal() {
		return 1;
	}
	
	static public class Request {

		private String taskId = "12";
		private String demandId = "111";
		private String parent = "124";
		private String assigneee = "hongjian";
		private int cost = 13;
		private int actionId = 2;
		
		
		public String getTaskId() {
			return taskId;
		}
		public String getDemandId() {
			return demandId;
		}
		public String getParent() {
			return parent;
		}
		public String getAssigneee() {
			return assigneee;
		}
    
		public int getCost() {
			return cost;
		}
		@Override
		public String toString() {
			return "Request [taskId=" + taskId + ", demandId=" + demandId + ", parent=" + parent + ", assigneee="
					+ assigneee + "]";
		}
		
	}

}
