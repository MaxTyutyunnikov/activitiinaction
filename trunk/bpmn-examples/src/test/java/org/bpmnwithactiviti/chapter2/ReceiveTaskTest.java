package org.bpmnwithactiviti.chapter2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskTest {

	@Test
	public void testWaitStateBehavior() {
		ProcessEngine processEngine = ProcessEngineConfiguration
			.createStandaloneInMemProcessEngineConfiguration()
	 		.buildProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		repositoryService.createDeployment()
				.addClasspathResource("chapter2/receiveTask.bpmn20.xml").deploy();

		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey("receiveTaskTest");
		Execution execution = runtimeService.createExecutionQuery()
				.processInstanceId(pi.getId()).activityId("waitState")
				.singleResult();
		assertNotNull(execution);
		runtimeService.signal(execution.getId());
		pi = runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();
		assertNull(pi);
	}
}