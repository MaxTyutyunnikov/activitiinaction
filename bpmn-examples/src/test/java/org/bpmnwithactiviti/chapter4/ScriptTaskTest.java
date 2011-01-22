package org.bpmnwithactiviti.chapter4;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ScriptTaskTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	@Deployment(resources={"chapter4/scripttask.bpmn20.xml"})
	public void creditCheckTrue() {
		assertEquals(true, creditCheck(100,10));
	}

	@Test
	@Deployment(resources={"chapter4/scripttask.bpmn20.xml"})
	public void creditCheckFalse() {
		assertEquals(false, creditCheck(10,100));
	}

	private boolean creditCheck(int income, int loanAmount) {
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("income", new Integer(income));
		processVariables.put("loanAmount", new Integer(loanAmount));
		ProcessInstance pi = activitiRule.getRuntimeService().startProcessInstanceByKey("scriptTest", processVariables);
		processVariables = activitiRule.getRuntimeService().getVariables(pi.getId());
		return (Boolean) processVariables.get("creditCheckOk");
	}
}