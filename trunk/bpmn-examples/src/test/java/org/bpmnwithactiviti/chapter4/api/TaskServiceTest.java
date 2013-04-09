package org.bpmnwithactiviti.chapter4.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class TaskServiceTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	private void startProcessInstance() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.bpmn20.xml"})
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
		// Create the user task
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.newTask();
		task.setName("Test task");
		task.setPriority(100);
		taskService.saveTask(task);
		assertNull(task.getAssignee());
		// Create the user
		IdentityService identityService = activitiRule.getIdentityService();
		User user = identityService.newUser("JohnDoe");
		identityService.saveUser(user);
		// Set the candidate user for the task
		taskService.addCandidateUser(task.getId(), "JohnDoe");
		// Claim and complete the task
		task = taskService.createTaskQuery().taskCandidateUser("JohnDoe").singleResult();
		assertNotNull(task);
		assertEquals("Test task", task.getName());
		assertNull(task.getAssignee());
		taskService.claim(task.getId(), "JohnDoe");
		task = taskService.createTaskQuery().taskAssignee("JohnDoe").singleResult();
		assertEquals("JohnDoe", task.getAssignee());
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().taskAssignee("JohnDoe").singleResult();
		assertNull(task);
	}
	
	@Test
	public void createTaskWithCandidateGroup() {
		// Create the user task
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.newTask();
		task.setName("Test task");
		task.setPriority(100);
		taskService.saveTask(task);
		assertNull(task.getAssignee());
		// Create users and group
		IdentityService identityService = activitiRule.getIdentityService();
		User user = identityService.newUser("JohnDoe1");
		identityService.saveUser(user);
		user = identityService.newUser("JohnDoe2");
		identityService.saveUser(user);
		Group group  = identityService.newGroup("JohnDoes");
		identityService.saveGroup(group);
		identityService.createMembership("JohnDoe1", group.getId());
		identityService.createMembership("JohnDoe2", group.getId());
		// Set the candidate group for the task
		taskService.addCandidateGroup(task.getId(), group.getId());
		// Claim and complete the task
		task = taskService.createTaskQuery().taskCandidateUser("JohnDoe1").singleResult();
		assertNotNull(task);
		assertEquals("Test task", task.getName());
		assertNull(task.getAssignee());
		taskService.claim(task.getId(), "JohnDoe1");
		task = taskService.createTaskQuery().taskCandidateUser("JohnDoe2").singleResult();
		assertNull(task);
		task = taskService.createTaskQuery().taskAssignee("JohnDoe1").singleResult();
		assertEquals("JohnDoe1", task.getAssignee());
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().taskAssignee("JohnDoe1").singleResult();
		assertNull(task);
	}

}
