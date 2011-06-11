package org.bpmnwithactiviti.chapter12;

import java.util.LinkedList;
import java.util.Queue;

import junit.framework.Assert;

import org.bpmnwithactiviti.chapter12.bam.event.LoanRequestProcessedEvent;
import org.bpmnwithactiviti.chapter12.bam.event.LoanRequestReceivedEvent;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

public class EsperTester {
	
	private EPRuntime epRuntime;
	private EPAdministrator epAdmin;

	@Before
	public void Setup() {
		Configuration configuration = new Configuration();
		configuration.addEventTypeAutoName("org.bpmnwithactiviti.chapter12.bam.event");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epRuntime = epService.getEPRuntime();
		epAdmin = epService.getEPAdministrator();
	}
	
	private Queue<Double> avgRequestedAmountQueue = new LinkedList<Double>();
	private Queue<Integer> maxRequestedAmountQueue = new LinkedList<Integer>();
	private Queue<Integer> sumRequestedAmountQueue = new LinkedList<Integer>();

	@Test
	public void testMonitoringRequestedAmount()
	{
		System.out.println("---------- Start monitoring requested amount ----------");
		
		EPStatement epStatement = epAdmin.createEPL(
			"select avg(requestedAmount) as avgRequestedAmount, max(requestedAmount) as maxRequestedAmount, sum(requestedAmount) as sumRequestedAmount" +
			" from LoanRequestReceivedEvent.win:time(1 sec)");
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Assert.assertEquals(1, newEvents.length);
				Assert.assertNull(oldEvents);
				Double avgRequestedAmount = (Double) newEvents[0].get("avgRequestedAmount");
				Integer maxRequestedAmount = (Integer) newEvents[0].get("maxRequestedAmount");
				Integer sumRequestedAmount = (Integer) newEvents[0].get("sumRequestedAmount");
				System.out.println("<<< avgRequestedAmount="+avgRequestedAmount+", maxRequestedAmount="+maxRequestedAmount+", sumRequestedAmount="+sumRequestedAmount);
				avgRequestedAmountQueue.add(avgRequestedAmount);
				maxRequestedAmountQueue.add(maxRequestedAmount);
				sumRequestedAmountQueue.add(sumRequestedAmount);
			}
		} );
		
		assertMRA(null, null, null);
		sendLoanRequestReceivedEvent (   0, "1", 100);
		assertMRA(100.0, 100, 100);
		sendLoanRequestReceivedEvent ( 300, "2", 200);
		assertMRA(150.0, 200, 300);
		sendLoanRequestProcessedEvent( 500, "2", true);
		assertMRA(null, null, null);
		sendLoanRequestProcessedEvent( 600, "1", false);
		assertMRA(null, null, null);
		sendLoanRequestReceivedEvent (1100, "3", 300);
		assertMRA(200.0, 200, 200);
		assertMRA(250.0, 300, 500);
		sendLoanRequestProcessedEvent(1400, "3", true);
		assertMRA(300.0, 300, 300);
		
		epStatement.destroy();
	}
		
	// Assert Monitored Request Amount
	private void assertMRA(Double avgRequestedAmount, Integer maxRequestedAmount, Integer sumRequestedAmount) {
		Assert.assertEquals(avgRequestedAmount, avgRequestedAmountQueue.poll());
		Assert.assertEquals(maxRequestedAmount, maxRequestedAmountQueue.poll());
		Assert.assertEquals(sumRequestedAmount, sumRequestedAmountQueue.poll());
	}

	private Queue<Double> avgProcessDurationQueue = new LinkedList<Double>();
	private Queue<Long> maxProcessDurationQueue = new LinkedList<Long>();
	
	@Test
	public void testMonitoringProcessDuration() {
		System.out.println("---------- Start monitoring process duration ----------");

		EPStatement epStatement = epAdmin.createEPL( 
			"select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration," +
			" max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration" +
			" from pattern [" +
			"	every beginEvent=LoanRequestReceivedEvent" +
			"   -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)" +
			" ].win:time(1 sec)");
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Assert.assertEquals(1, newEvents.length);
				Assert.assertNull(oldEvents);
				Double avgProcessDuration = (Double) newEvents[0].get("avgProcessDuration");
				Long maxProcessDuration = (Long) newEvents[0].get("maxProcessDuration");
				System.out.println("<<< avgProcessDuration="+avgProcessDuration+", maxProcessDuration="+maxProcessDuration);
				avgProcessDurationQueue.add(avgProcessDuration);
				maxProcessDurationQueue.add(maxProcessDuration);
			}
		} );
		
		assertMPD(null, null);
		sendLoanRequestReceivedEvent (   0, "1", 100);
		assertMPD(null, null);
		sendLoanRequestReceivedEvent ( 300, "2", 200);
		assertMPD(null, null);
		sendLoanRequestProcessedEvent( 400, "2", true);
		assertMPD(100.0, 100L);
		sendLoanRequestProcessedEvent( 600, "1", false);
		assertMPD(350.0, 600L);
		sendLoanRequestReceivedEvent (1100, "3", 300);
		assertMPD(null, null);
		sendLoanRequestProcessedEvent(1400, "3", true);
		assertMPD(600.0, 600L);
		assertMPD(450.0, 600L);

		epStatement.destroy();
	}

	// Assert Monitored Process Duration
	private void assertMPD(Double avgProcessDuration, Long maxProcessDuration) {
		Assert.assertEquals(avgProcessDuration, avgProcessDurationQueue.poll());
		Assert.assertEquals(maxProcessDuration, maxProcessDurationQueue.poll());
	}

	@Test
	public void testMonitoringDisapprovedRequests() {
		// Monitor number of approved vs. disapproved loan requests.
		// ToDo
		
	}

	private void sendLoanRequestReceivedEvent(long time, String processInstanceId, int requestedAmount) {
		sendEvent(time, new LoanRequestReceivedEvent(processInstanceId, time, requestedAmount));
	}

	private void sendLoanRequestProcessedEvent(long time, String processInstanceId, boolean requestApproved) {
		sendEvent(time, new LoanRequestProcessedEvent(processInstanceId, time, requestApproved));
	}
	
	private void sendEvent(long time, Object event) {
		System.out.printf(">>> %1$4d : %2$s\n", time, event);
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(event);
	}
}
