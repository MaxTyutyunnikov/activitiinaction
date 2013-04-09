package org.bpmnwithactiviti.chapter5;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class MailTaskTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-mail.xml");

	@Test
	@Deployment(resources={"chapter5/testSimpleMail.bpmn20.xml"})
	public void sendMailLocalTest() throws Exception {
		Wiser wiser = new Wiser();
	    wiser.setPort(1025);
	    wiser.start();
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		activitiRule.getRuntimeService().startProcessInstanceByKey("simpleEmailProcess", processVariables);
		List<WiserMessage> messages = wiser.getMessages();
		assertEquals(1, messages.size());
		WiserMessage message = messages.get(0);
		MimeMessage mimeMessage = message.getMimeMessage();
		assertEquals("Hello Miss Piggy", mimeMessage.getHeader("Subject", null));
		wiser.stop();
	}
}