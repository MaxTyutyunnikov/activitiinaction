package org.bpmnwithactiviti.chapter3.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class TaskServiceTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	private void startProcessInstance() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
	}
	
	@Test
	@Deployment(resources={"chapter3/bookorder.bpmn20.xml"})
	public void queryTask() {
		startProcessInstance();
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("sales").singleResult();
		assertEquals("Complete order", task.getName());
		System.out.println("task id " + task.getId() +
				", name " + task.getName() +
				", def key " + task.getTaskDefinitionKey());
	}
	
	@Test
	public void createTask() {
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.newTask();
		task.setName("Test task");
		task.setPriority(100);
		taskService.saveTask(task);
		assertNull(task.getAssignee());
		IdentityService identityService = activitiRule.getIdentityService();
		User user = identityService.newUser("Jane Doe");
		identityService.saveUser(user);
		taskService.addCandidateUser(task.getId(), "Jane Doe");
		Task queryTask = taskService.createTaskQuery().taskCandidateUser("Jane Doe").singleResult();
		assertNotNull(queryTask);
		assertEquals("Test task", queryTask.getName());
		assertNull(task.getAssignee());
		taskService.claim(task.getId(), "Jane Doe");
		Task claimedTask = taskService.createTaskQuery().taskAssignee("Jane Doe").singleResult();
		assertEquals("Jane Doe", claimedTask.getAssignee());
		taskService.complete(task.getId());
		Task completedTask = taskService.createTaskQuery().taskAssignee("Jane Doe").singleResult();
		assertNull(completedTask);
	}
}
