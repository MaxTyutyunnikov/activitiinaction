package org.bpmnwithactiviti.chapter12;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class LoanRequestProcessWithBAM extends AbstractTest {	
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	@Test
	@Deployment(resources={"chapter12/loanrequest_withbam.bpmn20.xml"})
	public void testBAM() {
		final RuntimeService runtimeService = activitiRule.getRuntimeService();
		final TaskService taskService = activitiRule.getTaskService();
		
		// Start first loan request
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("income", 100);
		processVariables.put("loanAmount", 10);
		runtimeService.startProcessInstanceByKey("loanrequest_withbam", processVariables);
		
		// Evaluate first loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved",true);
		taskService.complete(taskService.createTaskQuery().singleResult().getId(), processVariables);
	}
}