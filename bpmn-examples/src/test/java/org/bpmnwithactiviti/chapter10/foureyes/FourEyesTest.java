package org.bpmnwithactiviti.chapter10.foureyes;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class FourEyesTest {
	
	@Test
	public void validateFourEyes() {
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg-mem.xml")
				.setProcessEngineName(ProcessEngines.NAME_DEFAULT)
				.buildProcessEngine();
		
		processEngine.getRepositoryService().createDeployment()
				.addClasspathResource("chapter10/foureyes/fourEyes.bpmn20.xml")
				.name("fourEyes")
				.deploy();
		
		processEngine.getRuntimeService().startProcessInstanceByKey("fourEyesProcess");
		
		TaskService taskService = processEngine.getTaskService();
		Task firstTask = taskService.createTaskQuery().singleResult();
		taskService.claim(firstTask.getId(), "kermit");
		taskService.complete(firstTask.getId());
		
		Task secondTask = taskService.createTaskQuery().singleResult();
		
		try {
			taskService.claim(secondTask.getId(), "kermit");
			fail("Expected claim error");
		} catch(ActivitiException e) {
			// claim error expected
		}
		
		secondTask = taskService.createTaskQuery().taskId(secondTask.getId()).singleResult();
		assertNull(secondTask.getAssignee());
		
		taskService.claim(secondTask.getId(), "gonzo");
		taskService.complete(secondTask.getId());
	}
}
