package org.bpmnwithactiviti.chapter1;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class BookOrderTest {
	
	@Test
	public void startBookOrder() {
		ProcessEngine processEngine = ProcessEngineConfiguration
			.createStandaloneProcessEngineConfiguration()
		 	.buildProcessEngine(); 
		 
		RuntimeService runtimeService = processEngine.getRuntimeService(); 
		RepositoryService repositoryService = processEngine.getRepositoryService();
		TaskService taskService = processEngine.getTaskService(); 
		repositoryService.createDeployment()
			.addClasspathResource("chapter1/bookorder.bpmn20.xml")
			.deploy();
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
				"bookorder", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " 
				+ processInstance.getProcessDefinitionId());
		Task task = taskService.createTaskQuery().taskCandidateUser("kermit").singleResult();
		assertNotNull(task);
		System.out.println("found task " + task.getName());
		taskService.complete(task.getId());
	}

}
