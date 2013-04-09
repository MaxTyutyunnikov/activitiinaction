package org.bpmnwithactiviti.chapter11.webservice;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class WebServiceTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@Test
	@Deployment(resources={"chapter11/webservice/customer.bpmn20.xml"})
	public void queryTask() {
	  Map<String, Object> variableMap = new HashMap<String, Object>();
	  variableMap.put("name", "Alfresco");
	  variableMap.put("contactperson", "Tom Baeyens");
	  ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("customer", variableMap);
	  Object responseValue = activitiRule.getRuntimeService().getVariable(processInstance.getProcessInstanceId(), "webserviceResponse");
	  assertEquals("Highlands 343", responseValue);
	}
}
