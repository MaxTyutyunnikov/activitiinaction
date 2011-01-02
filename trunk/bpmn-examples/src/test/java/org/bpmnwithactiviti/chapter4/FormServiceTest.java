package org.bpmnwithactiviti.chapter4;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class FormServiceTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	@Deployment(resources={"chapter4/startform.bpmn20.xml",
			"chapter4/loanRequest.form"})
	public void startFormSubmit() {
		activitiRule.getRuntimeService().startProcessInstanceByKey("startFormtest");
		FormService formservice = activitiRule.getProcessEngine().getFormService();
	    Map<String, String> formProperties = new HashMap<String, String>();
	    formProperties.put("name", "Miss Piggy");
	    ProcessInstance pi = formservice.submitStartFormData("startFormtest:1", formProperties);
	    Map<String, Object> processVariables = activitiRule.getRuntimeService()
		.getVariables(pi.getId());
		assertEquals("Miss Piggy", processVariables.get("name"));
	}

}