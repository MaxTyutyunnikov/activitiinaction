package org.bpmnwithactiviti.chapter3.java;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class JavaBpmnTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	private ProcessInstance startProcessInstance() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", Long.valueOf("123456"));
		return runtimeService.startProcessInstanceByKey("bookorder", variableMap);
	}
	
	@Test
	@Deployment(resources={"chapter3/bookorder.java.bpmn20.xml"})
	public void executeJavaService() {
		ProcessInstance processInstance = startProcessInstance();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	}
	
	@Test
	@Deployment(resources={"chapter3/bookorder.java.field.bpmn20.xml"})
	public void executeJavaServiceWithExtensions() {
		ProcessInstance processInstance = startProcessInstance();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	}
	
	@Test
	@Deployment(resources={"chapter3/bookorder.java.expression.bpmn20.xml"})
	public void executeJavaExpression() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		BookOrder bookOrder = new BookOrder();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", Long.valueOf("123456"));
		variableMap.put("bookOrder", bookOrder);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		Date validatetime = (Date) runtimeService.getVariable(processInstance.getId(), "validatetime");
		assertNotNull(validatetime);
		System.out.println("validatetime is " + validatetime);
	}
}
