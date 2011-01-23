package org.bpmnwithactiviti.chapter4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class LoanRequestTest {	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-mail.xml");
	
	private Wiser wiser;
	
	@Before
	public void startMailServer()
	{
		wiser = new Wiser();
	    wiser.setPort(1025);
	    wiser.start();
	}
	
	@After
	public void stopMailServer()
	{
		wiser.stop();
	}

	@Test
	@Deployment(resources={"chapter4/loanrequest.bpmn20.xml","chapter4/loanRequest.form","chapter4/handleLoanRequest.form"})
	public void creditCheckTrue() throws Exception {
		/* The following should work but ... does not
		FormService formservice = activitiRule.getProcessEngine().getFormService();
	    Map<String,String> formProperties = new HashMap<String,String>();
	    formProperties.put("name", "Miss Piggy");
	    formProperties.put("emailAddress","miss.piggy@localhost");
	    formProperties.put("income","100");
	    formProperties.put("loanAmount","10");
	    ProcessInstance pi = formservice.submitStartFormData("loanrequest:1", formProperties);
	    */
		Map<String,Object> processVariables = new HashMap<String,Object> ();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("emailAddress", "miss.piggy@localhost");
		processVariables.put("income", new Integer(100));
		processVariables.put("loanAmount", new Integer(10));
		ProcessInstance pi = activitiRule.getRuntimeService().startProcessInstanceByKey("loanrequest",processVariables);
	    processVariables = activitiRule.getRuntimeService().getVariables(pi.getId());
		LoanApplication la = (LoanApplication) processVariables.get("loanApplication");
		assertEquals(true, la.isCreditCheckOk());
		assertEquals(true, la.isRiskCheckOk());
		List<Task> tasks = activitiRule.getTaskService().createTaskQuery().taskAssignee("fozzie").list();
	    assertEquals(1, tasks.size());
	    Task task = tasks.get(0);
	    assertNotNull(activitiRule.getFormService().getRenderedTaskForm(task.getId()));
	    Map<String,String> formProperties = new HashMap<String,String>();
	    formProperties.put("requestApproved","true");
	    activitiRule.getFormService().submitTaskFormData(task.getId(), formProperties);
	    assertEquals(0, wiser.getMessages().size());
	}
	
	@Test
	@Deployment(resources={"chapter4/loanrequest.bpmn20.xml","chapter4/loanRequest.form","chapter4/handleLoanRequest.form"})
	public void creditCheckFalse() throws Exception {

		/* The following should work but ... does not
		FormService formservice = activitiRule.getProcessEngine().getFormService();
	    Map<String,String> formProperties = new HashMap<String,String>();
	    formProperties.put("name", "Miss Piggy");
	    formProperties.put("emailAddress","miss.piggy@localhost");
	    formProperties.put("income","10");
	    formProperties.put("loanAmount","100");
	    ProcessInstance pi = formservice.submitStartFormData("loanrequest:1", formProperties);
	    */
		Map<String,Object> processVariables = new HashMap<String,Object> ();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("emailAddress", "miss.piggy@localhost");
		processVariables.put("income", new Integer(10));
		processVariables.put("loanAmount", new Integer(100));
		ProcessInstance pi = activitiRule.getRuntimeService().startProcessInstanceByKey("loanrequest",processVariables);
	    processVariables = activitiRule.getRuntimeService().getVariables(pi.getId());
		LoanApplication la = (LoanApplication) processVariables.get("loanApplication");
		assertEquals(false, la.isCreditCheckOk());
		assertEquals(true, la.isRiskCheckOk());
		List<Task> tasks = activitiRule.getTaskService().createTaskQuery().taskAssignee("fozzie").list();
	    assertEquals(1, tasks.size());
	    Task task = tasks.get(0);
	    assertNotNull(activitiRule.getFormService().getRenderedTaskForm(task.getId()));
	    Map<String,String> formProperties = new HashMap<String,String>();
	    formProperties.put("requestApproved","false");
	    formProperties.put("motivation","Motivation for the rejection");
	    activitiRule.getFormService().submitTaskFormData(task.getId(), formProperties);
	    // activitiRule.getTaskService().complete(task.getId());
		List<WiserMessage> messages = wiser.getMessages();
	    assertEquals(1, messages.size());
	    WiserMessage message = messages.get(0);
	    MimeMessage mimeMessage = message.getMimeMessage();
	    assertEquals("Loan Request Denied", mimeMessage.getHeader("Subject", null));
	}
	
	// @Test
	@Deployment(resources={"chapter4/loanrequest.bpmn20.xml","chapter4/loanRequest.form","chapter4/handleLoanRequest.form"})
	public void testEscalation() throws Exception {
		Map<String,Object> processVariables = new HashMap<String,Object> ();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("emailAddress", "miss.piggy@localhost");
		processVariables.put("income", new Integer(100));
		processVariables.put("loanAmount", new Integer(10));
		activitiRule.getRuntimeService().startProcessInstanceByKey("loanrequest",processVariables);
		System.out.println("Testing escalation ... please wait for 80 seconds.");
	    assertEquals(1, activitiRule.getTaskService().createTaskQuery().taskAssignee("fozzie").list().size());
		Thread.sleep(80000);
	    assertEquals(0, activitiRule.getTaskService().createTaskQuery().taskAssignee("fozzie").list().size());
	    List<Task> tasks = activitiRule.getTaskService().createTaskQuery().taskCandidateGroup("management").list();
	    assertEquals(1, tasks.size());
	    Task task = tasks.get(0);
	    assertNotNull(activitiRule.getFormService().getRenderedTaskForm(task.getId()));
	    Map<String,String> formProperties = new HashMap<String,String>();
	    formProperties.put("requestApproved","true");
	    activitiRule.getFormService().submitTaskFormData(task.getId(), formProperties);
	    assertEquals(0, wiser.getMessages().size());
	}
}