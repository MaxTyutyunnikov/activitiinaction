package org.bpmnwithactiviti.chapter14;

import java.util.LinkedList;
import java.util.Queue;

import junit.framework.Assert;

import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestProcessedEvent;
import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestReceivedEvent;
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
import com.espertech.esper.client.time.TimerControlEvent;
import com.espertech.esper.client.time.TimerControlEvent.ClockType;

public class TimeWindowTest {
	
	private EPRuntime epRuntime;
	private EPAdministrator epAdmin;

	@Before
	public void startEsper() {
		Configuration configuration = new Configuration();
		configuration.addEventTypeAutoName("org.bpmnwithactiviti.chapter14.bam.event");
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epRuntime = epService.getEPRuntime();
		epAdmin = epService.getEPAdministrator();
	}
	
	private Queue<Long> numLoansQueue = new LinkedList<Long>();
	private Queue<Integer> sumLoanedAmountQueue = new LinkedList<Integer>();
	
	@Test
	public void monitorLoanedAmount() {
		System.out.println("---------- Start monitoring loaned amount ----------");
		
		epRuntime.sendEvent(new TimerControlEvent(ClockType.CLOCK_EXTERNAL));
		
		EPStatement epStatement = epAdmin.createEPL(
			"select count(*) as numLoans, sum(requestedAmount) as sumLoanedAmount from LoanRequestProcessedEvent(requestApproved=true).win:time(1 sec)");
		
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Assert.assertEquals(1, newEvents.length);
				Assert.assertNull(oldEvents);
				Long numLoans = (Long) newEvents[0].get("numLoans");
				Integer sumLoanedAmount = (Integer) newEvents[0].get("sumLoanedAmount");
				System.out.println("<<< numLoans="+numLoans+", sumLoanedAmount="+sumLoanedAmount);
				numLoansQueue.add(numLoans);
				sumLoanedAmountQueue.add(sumLoanedAmount);
			}
		} );
		
		sendLoanRequestProcessedEvent(1000, "1", true, 100);
		assertMonitoredLoans(1L, 100);
		sendLoanRequestProcessedEvent(1300, "2", true, 200);
		assertMonitoredLoans(2L, 300);
		sendLoanRequestProcessedEvent(1600, "3", false, 1000);
		assertMonitoredLoans(null, null);
		sendLoanRequestProcessedEvent(1900, "4", true, 300);
		assertMonitoredLoans(3L, 600);
		sendLoanRequestProcessedEvent(2200, "5", true, 400);
		assertMonitoredLoans(2L, 500);
		assertMonitoredLoans(3L, 900);
		sendLoanRequestProcessedEvent(2400, "6", false, 900);
		assertMonitoredLoans(2L, 700);
		assertMonitoredLoans(null, null);
		
		epStatement.destroy();
	}

	// Assert Monitored Loaned Amount
	private void assertMonitoredLoans(Long numLoans, Integer sumLoanedAmount) {
		Assert.assertEquals(numLoans, numLoansQueue.poll());
		Assert.assertEquals(sumLoanedAmount, sumLoanedAmountQueue.poll());
	}

	private Queue<Double> avgProcessDurationQueue = new LinkedList<Double>();
	private Queue<Long> maxProcessDurationQueue = new LinkedList<Long>();
	
	@Test
	public void monitorProcessDuration() {
		System.out.println("---------- Start monitoring process duration ----------");
		
		epRuntime.sendEvent(new TimerControlEvent(ClockType.CLOCK_EXTERNAL));

		EPStatement epStatement = epAdmin.createEPL(new StringBuffer()
				.append("select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration, ")
				.append("max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration ")
				.append("from pattern [every beginEvent=LoanRequestReceivedEvent -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)].win:time(5 sec)")
				.toString());
		
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
		
		sendLoanRequestReceivedEvent (   0, "1", 100);
		assertMonitoredProcessDuration(null, null);
		
		sendLoanRequestReceivedEvent ( 300, "2", 200);
		assertMonitoredProcessDuration(null, null);
		
		sendLoanRequestProcessedEvent( 400, "2", true, 200);
		assertMonitoredProcessDuration(100.0, 100L);
		
		sendLoanRequestProcessedEvent( 600, "1", true, 100);
		assertMonitoredProcessDuration(350.0, 600L);
		
		sendLoanRequestReceivedEvent (1100, "3", 300);
		assertMonitoredProcessDuration(null, null);
		
		sendLoanRequestProcessedEvent(1600, "3", true, 300);
		assertMonitoredProcessDuration(400.0, 600L);

		epStatement.destroy();
	}

	// Assert Monitored Process Duration
	private void assertMonitoredProcessDuration(Double avgProcessDuration, Long maxProcessDuration) {
		Assert.assertEquals(avgProcessDuration, avgProcessDurationQueue.poll());
		Assert.assertEquals(maxProcessDuration, maxProcessDurationQueue.poll());
	}

	private void sendLoanRequestReceivedEvent(long time, String processInstanceId, int requestedAmount) {
		sendEvent(time, new LoanRequestReceivedEvent(processInstanceId, time, requestedAmount));
	}

	private void sendLoanRequestProcessedEvent(long time, String processInstanceId, boolean requestApproved, int loanedAmount) {
		sendEvent(time, new LoanRequestProcessedEvent(processInstanceId, time, requestApproved, loanedAmount));
	}

	private void sendEvent(long time, Object event) {
		System.out.printf(">>> %1$4d : %2$s\n", time, event);
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(event);
	}
}
