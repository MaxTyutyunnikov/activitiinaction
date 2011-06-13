package org.bpmnwithactiviti.chapter12;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import com.espertech.esper.client.UpdateListener;

public class LoanRequestProcessWithEsperTest extends AbstractTest {	
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	private static String REQUESTED_AMOUNT_STATEMENT_NAME = "requestedAmount";
	private static String LOANED_AMOUNT_STATEMENT_NAME = "loanedAmount";
	private static String PROCESS_DURATION_STATEMENT_NAME = "processDuration";
	private EPAdministrator epAdmin;
	
	@Before
	public void Setup() {
		Configuration configuration = new Configuration();
		configuration.addEventTypeAutoName("org.bpmnwithactiviti.chapter12.bam.event");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epAdmin = epService.getEPAdministrator();
		
		epAdmin.createEPL(
			"select avg(requestedAmount) as avgRequestedAmount, max(requestedAmount) as maxRequestedAmount, sum(requestedAmount) as sumRequestedAmount" +
			" from LoanRequestReceivedEvent.win:length(2)",REQUESTED_AMOUNT_STATEMENT_NAME);
		
		epAdmin.createEPL(
			"select count(*) as numLoans, sum(requestedAmount) as sumLoanedAmount from LoanRequestProcessedEvent(requestApproved=true).win:length(2)",LOANED_AMOUNT_STATEMENT_NAME);

		epAdmin.createEPL( 
			"select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration," +
			" max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration" +
			" from pattern [" +
			"	every beginEvent=LoanRequestReceivedEvent" +
			"   -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)" +
			" ].win:length(2)",PROCESS_DURATION_STATEMENT_NAME);
	}

	@Test
	@Deployment(resources={"chapter12/loanrequest_withespertest.bpmn20.xml"})
	public void firstTest() {
		final RuntimeService runtimeService = activitiRule.getRuntimeService();
		final TaskService taskService = activitiRule.getTaskService();
		
		// Start first loan request
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("income", 100);
		processVariables.put("loanAmount", 10);
		runtimeService.startProcessInstanceByKey("loanrequest_withespertest", processVariables);
		
		// The monitor is started here to illustrate the case when the monitor is started after process instances are already created.
		startMonitor();
		assertMonitoredRequestedAmountEquals(10.0, 10, 10);
		assertMonitoredLoanesEquals(0L, null);
		assertMonitoredProcessDurationNull();

		// Evaluate first loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved",true);
		taskService.complete(taskService.createTaskQuery().singleResult().getId(), processVariables);
		assertMonitoredLoanesEquals(1L, 10);
		assertMonitoredProcessDurationNotNull();

		// Start second loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Kermit");
		processVariables.put("income", 200);
		processVariables.put("loanAmount", 20);
		runtimeService.startProcessInstanceByKey("loanrequest_withespertest", processVariables);
		assertMonitoredRequestedAmountEquals(15.0, 20, 30);

		// Evaluate second loan request
		processVariables = new HashMap<String, Object>();
		processVariables.put("requestApproved",true);
		taskService.complete(taskService.createTaskQuery().singleResult().getId(), processVariables);
		assertMonitoredLoanesEquals(2L, 30);
		assertMonitoredProcessDurationNotNull();
	}
		
	// Monitored values 
	private Double avgRequestedAmount;
	private Integer maxRequestedAmount;
	private Integer sumRequestedAmount;
	
	private Long numLoans;
	private Integer sumLoanedAmount;
	private Double avgProcessDuration;
	
	private Long maxProcessDuration;
	
	private void startMonitor() {
		// Start monitoring requested amount
		EPStatement epStatement = epAdmin.getStatement(REQUESTED_AMOUNT_STATEMENT_NAME);
		SafeIterator<EventBean> eventIterator = epStatement.safeIterator();
		try 
		{
			EventBean event = eventIterator.next();
			avgRequestedAmount = (Double) event.get("avgRequestedAmount");
			maxRequestedAmount = (Integer) event.get("maxRequestedAmount");
			sumRequestedAmount = (Integer) event.get("sumRequestedAmount");
			showMonitoredRequestedAmountInfo();
		}
		finally {
		  eventIterator.close(); // Note: safe iterators must be closed
		}
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				avgRequestedAmount = (Double) newEvents[0].get("avgRequestedAmount");
				maxRequestedAmount = (Integer) newEvents[0].get("maxRequestedAmount");
				sumRequestedAmount = (Integer) newEvents[0].get("sumRequestedAmount");
				showMonitoredRequestedAmountInfo();
			}
		} );
		
		// Start monitoring loaned amount
		epStatement = epAdmin.getStatement(LOANED_AMOUNT_STATEMENT_NAME);
		eventIterator = epStatement.safeIterator();
		try 
		{
			EventBean event = eventIterator.next();
			numLoans = (Long) event.get("numLoans");
			sumLoanedAmount = (Integer) event.get("sumLoanedAmount");
			showMonitoredLoanedAmountInfo();
		}
		finally {
		  eventIterator.close(); // Note: safe iterators must be closed
		}
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				numLoans = (Long) newEvents[0].get("numLoans");
				sumLoanedAmount = (Integer) newEvents[0].get("sumLoanedAmount");
				showMonitoredLoanedAmountInfo();
			}
		} );

		// Start monitoring process duration
		epStatement = epAdmin.getStatement(PROCESS_DURATION_STATEMENT_NAME);
		eventIterator = epStatement.safeIterator();
		try 
		{
			EventBean event = eventIterator.next();
			avgProcessDuration = (Double) event.get("avgProcessDuration");
			maxProcessDuration = (Long) event.get("maxProcessDuration");
			showMonitoredProcessDurationInfo();
		}
		finally {
		  eventIterator.close(); // Note: safe iterators must be closed
		}
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
				maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
				showMonitoredProcessDurationInfo();
			}
		} );
	}
	
	private void showMonitoredRequestedAmountInfo() {
		System.out.println("<<< avgRequestedAmount="+avgRequestedAmount+", maxRequestedAmount="+maxRequestedAmount+", sumRequestedAmount="+sumRequestedAmount);
	}

	private void showMonitoredLoanedAmountInfo() {
		System.out.println("<<< numLoans="+numLoans+", sumLoanedAmount="+sumLoanedAmount);
	}
	
	private void showMonitoredProcessDurationInfo() {
		System.out.println("<<< avgProcessDuration="+avgProcessDuration+", maxProcessDuration="+maxProcessDuration);
	}
	
	private void assertMonitoredRequestedAmountEquals(Double avgRequestedAmount, Integer maxRequestedAmount, Integer sumRequestedAmount) {
		Assert.assertEquals(avgRequestedAmount, this.avgRequestedAmount);
		Assert.assertEquals(maxRequestedAmount, this.maxRequestedAmount);
		Assert.assertEquals(sumRequestedAmount, this.sumRequestedAmount);
	}

	private void assertMonitoredLoanesEquals(Long numLoans, Integer sumLoanedAmount) {
		Assert.assertEquals(numLoans, this.numLoans);
		Assert.assertEquals(sumLoanedAmount, this.sumLoanedAmount);
	}

	private void assertMonitoredProcessDurationNull() {
		Assert.assertNull(this.avgProcessDuration);
		Assert.assertNull(this.maxProcessDuration);
	}

	private void assertMonitoredProcessDurationNotNull() {
		Assert.assertNotNull(this.avgProcessDuration);
		Assert.assertNotNull(this.maxProcessDuration);
	}
}