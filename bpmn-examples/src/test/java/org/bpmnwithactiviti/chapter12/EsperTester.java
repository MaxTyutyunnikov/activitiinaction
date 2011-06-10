package org.bpmnwithactiviti.chapter12;

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
	
	@Test
	public void testMonitoringIncome()
	{
		// Monitor requestedAmount
		EPStatement epStatement = epAdmin.createEPL(
			"select avg(requestedAmount) as avgRequestedAmount, max(requestedAmount) as maxRequestedAmount, sum(requestedAmount) as sumRequestedAmount" +
			" from LoanRequestReceivedEvent.win:time(1 sec)");
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Assert.assertEquals(1, newEvents.length);
				Assert.assertNull(oldEvents);
				System.out.println("<<< avgRequestedAmount="+newEvents[0].get("avgRequestedAmount")+
								     ", maxRequestedAmount="+newEvents[0].get("maxRequestedAmount")+
								     ", sumRequestedAmount="+newEvents[0].get("sumRequestedAmount"));
			}
		} );
		
		sendTestEvents();
	}
		
	@Test
	public void testMonitoringProcessDuration() {
		// Monitor process duration.
		EPStatement epStatement = epAdmin.createEPL( 
			"select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration," +
			" max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration" +
			" from pattern [" +
			"	every beginEvent=LoanRequestReceivedEvent" +
			"   -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)" +
			" ].win:time(1 hour)");
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				Assert.assertEquals(1, newEvents.length);
				Assert.assertNull(oldEvents);
				System.out.println("<<< avgProcessDuration="+newEvents[0].get("avgProcessDuration")+
					     		     ", maxProcessDuration="+newEvents[0].get("maxProcessDuration"));
			}
		} );

		// Monitor number of approved vs. disapproved loan requests.
		// ToDo
		
		// Monitor "Evaluate Loan Request" task workload.
		// ToDo

		// Monitor "Evaluate Loan Request" task duration.
		// ToDo
	}


	private void sendTestEvents() {
		sendLoanRequestReceivedEvent (   0, "1", 100);
		sendLoanRequestReceivedEvent ( 300, "2", 200);
		sendLoanRequestProcessedEvent( 500, "2", true);
		sendLoanRequestProcessedEvent( 600, "1", false);
		sendLoanRequestReceivedEvent (1100, "3", 300);
		sendLoanRequestProcessedEvent(1400, "3", true);
	}
	
	private void sendLoanRequestReceivedEvent(long time, String processInstanceId, int requestedAmount) {
		sendEvent(time, new LoanRequestReceivedEvent(processInstanceId, time, requestedAmount));
	}

	private void sendLoanRequestProcessedEvent(long time, String processInstanceId, boolean requestApproved) {
		sendEvent(time, new LoanRequestProcessedEvent(processInstanceId, time, requestApproved));
	}
	
	private void sendEvent(long time, Object event) {
		System.out.println(">>> "+time+" : "+event);
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(event);
	}
}
