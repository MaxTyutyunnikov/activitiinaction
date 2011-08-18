package org.bpmnwithactiviti.chapter4.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class IdentityServiceTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@Test
	@Deployment(resources = {"chapter4/bookorder.bpmn20.xml"})
	public void testMembership() {
		User newUser = activitiRule.getIdentityService().newUser("John Doe");
		activitiRule.getIdentityService().saveUser(newUser);
		User user = activitiRule.getIdentityService().createUserQuery().singleResult();
		assertEquals("John Doe", user.getId());
		
		Group newGroup = activitiRule.getIdentityService().newGroup("sales");
		newGroup.setName("Sales");
		activitiRule.getIdentityService().saveGroup(newGroup);
		Group group = activitiRule.getIdentityService().createGroupQuery().singleResult();
		assertEquals("Sales", group.getName());
		
		IdentityService identityService = activitiRule.getIdentityService();
		identityService.createMembership("John Doe", "sales");
		
		identityService.setAuthenticatedUserId("John Doe");
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateUser("John Doe").singleResult();
		assertNotNull(task);
		assertEquals("Complete order", task.getName());
	}
}
