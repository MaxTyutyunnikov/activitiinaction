package org.bpmnwithactiviti.chapter3.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.BeforeClass;
import org.junit.Test;

public class RuntimeServiceTest {
	
	private static RuntimeService runtimeService;
	
	@BeforeClass
	public static void init() {
		ProcessEngine processEngine = ProcessEngineConfiguration
			.createStandaloneInMemProcessEngineConfiguration()
			.setDatabaseSchemaUpdate("true")
	 		.buildProcessEngine();
		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment()
			.addClasspathResource("chapter3/bookorder.bpmn20.xml")
			.deploy();
		
		runtimeService = processEngine.getRuntimeService();
	}
	
	@Test
	public void startProcessInstance() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " 
				+ processInstance.getProcessDefinitionId());
	}
	
	@Test
	public void queryProcessInstance() {
		List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().processDefinitionKey("bookorder").list();
		for (ProcessInstance queryProcessInstance : instanceList) {
			assertEquals(false, queryProcessInstance.isEnded());
			System.out.println("id " + queryProcessInstance.getId() + 
					", ended=" + queryProcessInstance.isEnded());
		}
	}
	
	@Test
	public void queryProcessExecution() {
		Execution execution = runtimeService.createExecutionQuery().processDefinitionKey("bookorder").singleResult();
		assertNotNull(execution);
		assertEquals(false, execution.isEnded());
		System.out.println("id " + execution.getId() + ", ended=" + execution.isEnded());
		List<String> activityList = runtimeService.getActiveActivityIds(execution.getId());
		assertNotNull(activityList);
		assertEquals(1, activityList.size());
		System.out.println("Current activity id " + activityList.get(0));
	}
}
