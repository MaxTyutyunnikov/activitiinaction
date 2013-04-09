package org.bpmnwithactiviti.chapter14;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class LoanRequestProcessWithBAMTest {	
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");

	private RuntimeService runtimeService;
	private TaskService taskService;
	private Random random;

	@Test
	@Deployment(resources={"chapter14/loanrequest_withbam.bpmn20.xml"})
	public void testBAM() throws InterruptedException {
		runtimeService = activitiRule.getRuntimeService();
		taskService = activitiRule.getTaskService();
		random = new Random();

		for (int i = 0; i < 20; i++) {
			System.out.println(">>> Creating process: "+i);
			startRandomLoanRequestProcess("Person "+Integer.toString(i));
		}
	}
		
	private void startRandomLoanRequestProcess(String name) throws InterruptedException {
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", name);
		processVariables.put("income", 1000);
		processVariables.put("loanAmount", random.nextInt(100));
		runtimeService.startProcessInstanceByKey("loanrequest_withbam", processVariables);
		
		// Evaluate first loan request
		Thread.sleep(500+random.nextInt(1000));
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved",true);
		taskService.complete(taskService.createTaskQuery().singleResult().getId(), processVariables);
	}
}