package org.bpmnwithactiviti.chapter1;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class SimpleProcessTest {
	
	@Test
	public void startBookOrder() {
		java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");  
		Handler[] handlers = rootLogger.getHandlers();  
		for (int i = 0; i < handlers.length; i++) {  
			rootLogger.removeHandler(handlers[i]);  
		}
		SLF4JBridgeHandler.install();
		ProcessEngine processEngine = ProcessEngineConfiguration
			.createStandaloneInMemProcessEngineConfiguration()
			.setDatabaseSchemaUpdate("true")
			.setJobExecutorActivate(false)
		 	.buildProcessEngine(); 
		 
		RuntimeService runtimeService = processEngine.getRuntimeService(); 
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeployment()
			.addClasspathResource("chapter1/bookorder.simple.bpmn20.xml")
			.deploy();
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
				"simplebookorder");
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " 
				+ processInstance.getProcessDefinitionId());
		repositoryService.deleteDeploymentCascade(deployment.getId());
	}

}
