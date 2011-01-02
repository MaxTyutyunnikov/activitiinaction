package org.bpmnwithactiviti.chapter4;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
	@Deployment(resources={"chapter4/testSimpleTextMail.bpmn20.xml"})
	public void bookAvalaible() throws Exception {
		Wiser wiser = new Wiser();
	    wiser.setPort(1025);
	    wiser.start();
		activitiRule.getRuntimeService().startProcessInstanceByKey("simpleEmailProcess");
		List<WiserMessage> messages = wiser.getMessages();
	    assertEquals(1, messages.size());
	    
	    WiserMessage message = messages.get(0);
	    MimeMessage mimeMessage = message.getMimeMessage();
	    assertEquals("Hello Miss Piggy!", mimeMessage.getHeader("Subject", null));
		wiser.stop();
	}

}