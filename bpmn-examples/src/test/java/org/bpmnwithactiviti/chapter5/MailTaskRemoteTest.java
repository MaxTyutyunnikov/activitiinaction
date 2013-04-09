package org.bpmnwithactiviti.chapter5;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class MailTaskRemoteTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-mail.xml");

	@Test
	@Deployment(resources={"chapter5/testSimpleMail.bpmn20.xml"})
	public void sendMailRemoteTest() throws Exception {
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		activitiRule.getRuntimeService().startProcessInstanceByKey("simpleEmailProcess", processVariables);
	}
}