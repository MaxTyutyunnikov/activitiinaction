package org.bpmnwithactiviti.chapter2;

import static org.junit.Assert.assertNotNull;
import junit.framework.AssertionFailedError;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class WaitStateTest {

	@Test
	public void testWaitState() {
		ProcessEngine processEngine = ProcessEngineConfiguration
			.createStandaloneInMemProcessEngineConfiguration()
			.setDatabaseSchemaUpdate("true")
			.setJobExecutorActivate(false)
	 		.buildProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		repositoryService.createDeployment()
				.addClasspathResource("chapter2/waitstateTask.bpmn20.xml").deploy();

		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey("waitStateTask");
		Execution execution = runtimeService.createExecutionQuery()
				.processInstanceId(pi.getId()).activityId("waitState")
				.singleResult();
		assertNotNull(execution);
		runtimeService.signal(execution.getId());
		assertProcessEnded(processEngine, pi.getId());
	}

	public void assertProcessEnded(ProcessEngine processEngine,
			final String processInstanceId) {
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		if (processInstance != null) {
			throw new AssertionFailedError(
					"expected finished process instance '" + processInstanceId
							+ "' but it was still in the db");
		}
	}

}