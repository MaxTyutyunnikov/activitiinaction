package org.bpmnwithactiviti.chapter14;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class LoanRequestProcessWithEsperTest {	
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	private EPAdministrator epAdmin;
	
	@Before
	public void startEsper() {
		Configuration configuration = new Configuration();
		configuration.addEventTypeAutoName("org.bpmnwithactiviti.chapter14.bam.event");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epAdmin = epService.getEPAdministrator();
		
		epAdmin.createEPL( 
				"select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration" +
				" from pattern [" +
				"	every beginEvent=LoanRequestReceivedEvent" +
				"   -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)" +
				" ].win:length(20)", "processDuration");
	}
	
	private Double avgProcessDuration = null;

	@Test
	@Deployment(resources={"chapter14/loanrequest_withespertest.bpmn20.xml"})
	public void testEsperActivitiSetup() throws Exception {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		
		EPStatement epStatement = epAdmin.getStatement("processDuration");
	  epStatement.addListener(new UpdateListener () {
	    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	      avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
	    }
	  });
		
		// Start first loan request
	  Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("loanAmount", 10);
		runtimeService.startProcessInstanceByKey("loanrequest_withespertest", processVariables);
		
		Thread.sleep(1000);

		// Evaluate first loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved", true);
		taskService.complete(taskService.createTaskQuery().singleResult().getId(), processVariables);
		
		System.out.println("<<< avgProcessDuration = " + avgProcessDuration);
		assertTrue(avgProcessDuration >= 1000);

		// Start second loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("loanAmount", 20);
		runtimeService.startProcessInstanceByKey("loanrequest_withespertest", processVariables);
		
		Thread.sleep(2000);

		// Evaluate second loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved", true);
		taskService.complete(taskService.createTaskQuery().singleResult().getId(), processVariables);
		
		System.out.println("<<< avgProcessDuration = " + avgProcessDuration);
		assertTrue(avgProcessDuration >= 1500);
	}
}