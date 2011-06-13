package org.bpmnwithactiviti.chapter12;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.UpdateListener;

public class LoanRequestProcessWithBAMTest extends AbstractTest {	
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	private static String REQUESTED_AMOUNT_STATEMENT_NAME = "requestedAmount";
	private static String LOANED_AMOUNT_STATEMENT_NAME = "loanedAmount";
	private static String PROCESS_DURATION_STATEMENT_NAME = "processDuration";
	private EPRuntime epRuntime;
	private EPAdministrator epAdmin;

	@Before
	public void Setup() {
		Configuration configuration = new Configuration();
		configuration.addEventTypeAutoName("org.bpmnwithactiviti.chapter12.bam.event");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epRuntime = epService.getEPRuntime();
		epAdmin = epService.getEPAdministrator();
		
		epAdmin.createEPL(
			"select avg(requestedAmount) as avgRequestedAmount, max(requestedAmount) as maxRequestedAmount, sum(requestedAmount) as sumRequestedAmount" +
			" from LoanRequestReceivedEvent.win:length(3)",REQUESTED_AMOUNT_STATEMENT_NAME);
		
		epAdmin.createEPL(
			"select count(*) as numLoans, sum(requestedAmount) as sumLoanedAmount from LoanRequestProcessedEvent(requestApproved=true).win:time(1 sec)",LOANED_AMOUNT_STATEMENT_NAME);

		epAdmin.createEPL( 
			"select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration," +
			" max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration" +
			" from pattern [" +
			"	every beginEvent=LoanRequestReceivedEvent" +
			"   -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)" +
			" ].win:time(1 sec)",PROCESS_DURATION_STATEMENT_NAME);
	}

	@Test
	@Deployment(resources={"chapter12/loanrequest_withbam.bpmn20.xml"})
	public void firstTest() {
		epAdmin.getStatement(REQUESTED_AMOUNT_STATEMENT_NAME).addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Double avgRequestedAmount = (Double) newEvents[0].get("avgRequestedAmount");
				Integer maxRequestedAmount = (Integer) newEvents[0].get("maxRequestedAmount");
				Integer sumRequestedAmount = (Integer) newEvents[0].get("sumRequestedAmount");
				System.out.println("<<< avgRequestedAmount="+avgRequestedAmount+", maxRequestedAmount="+maxRequestedAmount+", sumRequestedAmount="+sumRequestedAmount);
			}
		} );
		
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("income", 100);
		processVariables.put("loanAmount", 10);
		activitiRule.getRuntimeService().startProcessInstanceByKey("loanrequest_withbam", processVariables);
		assertEquals(1, activitiRule.getRuntimeService().createProcessInstanceQuery().count());		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("Evaluate loan request", task.getName());
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved",true);
		taskService.complete(task.getId(), processVariables);
		assertEquals(0, activitiRule.getRuntimeService().createProcessInstanceQuery().count());
	}
}