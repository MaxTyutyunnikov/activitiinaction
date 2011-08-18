package org.bpmnwithactiviti.chapter4.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class RepositoryServiceTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@Test
	public void deleteDeployment() {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		String deploymentID = repositoryService.createDeployment()
			.addClasspathResource("chapter3/bookorder.bpmn20.xml")
			.deploy()
			.getId();
		
		Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
		assertNotNull(deployment);
		assertEquals(deploymentID, deployment.getId());
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.latestVersion().singleResult();
		assertNotNull(processDefinition);
		assertEquals("bookorder", processDefinition.getKey());
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().singleResult();
		assertNotNull(processInstance);
		assertEquals(processDefinition.getId(), processInstance.getProcessDefinitionId());
		
		repositoryService.deleteDeploymentCascade(deploymentID);
		
		deployment = repositoryService.createDeploymentQuery().singleResult();
		assertNull(deployment);
		processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		assertNull(processDefinition);
		processInstance = runtimeService.createProcessInstanceQuery().singleResult();
		assertNull(processInstance);
	}
}
