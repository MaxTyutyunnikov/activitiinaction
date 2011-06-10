package org.bpmnwithactiviti.chapter12;

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
	
	private EPServiceProvider epService;

	private static final long MS_HOUR = 60 * 60 * 1000; // Number of milliseconds per hour
	
	@Before
	public void Setup() {
		Configuration configuration = new Configuration();
		configuration.addEventTypeAutoName("org.bpmnwithactiviti.chapter12.bam.event");
		epService = EPServiceProviderManager.getDefaultProvider(configuration);
	}
	
	@Test
	public void testMonitoringIncome()
	{
		EPAdministrator epAdmin = epService.getEPAdministrator();

		// Monitor income
		EPStatement epStatement = epAdmin.createEPL(
			"select avg(income) as avgIncome, max(income) as maxIncome, sum(income) as sumIncome" +
			" from LoanRequestReceivedEvent.win:time(1 day)");
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				System.out.println(">>> avgIncome="+newEvents[0].get("avgIncome")+
								     ", maxIncome="+newEvents[0].get("maxIncome")+
								     ", sumIncome="+newEvents[0].get("sumIncome"));
			}
		} );
		
		// Monitor requestedAmount
		epStatement = epAdmin.createEPL(
			"select avg(requestedAmount) as avgRequestedAmount, max(requestedAmount) as maxRequestedAmount, sum(requestedAmount) as sumRequestedAmount" +
			" from LoanRequestReceivedEvent.win:time(4 hours)");
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				System.out.println(">>> avgRequestedAmount="+newEvents[0].get("avgRequestedAmount")+
								     ", maxRequestedAmount="+newEvents[0].get("maxRequestedAmount")+
								     ", sumRequestedAmount="+newEvents[0].get("sumRequestedAmount"));
			}
		} );
		
		// Monitor process duration.
		epStatement = epAdmin.createEPL( 
			"select avg(endEvent.processedTime - beginEvent.receiveTime) as avgProcessDuration," +
			" max(endEvent.processedTime - beginEvent.receiveTime) as maxProcessDuration" +
			" from pattern [" +
			"	every beginEvent=LoanRequestReceivedEvent" +
			"   -> endEvent=LoanRequestProcessedEvent(processInstanceId=beginEvent.processInstanceId)" +
			" ]");
		epStatement.addListener(new UpdateListener () {
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				System.out.println(">>> avgProcessDuration="+newEvents[0].get("avgProcessDuration")+
					     		     ", maxProcessDuration="+newEvents[0].get("maxProcessDuration"));
			}
		} );

		// Monitor number of approved vs. disapproved loan requests.
		// ToDo
		
		// Monitor "Evaluate Loan Request" task workload.
		// ToDo

		// Monitor "Evaluate Loan Request" task duration.
		// ToDo

		EPRuntime epRuntime = epService.getEPRuntime();

		// Reset the clock which Esper uses for the time windows.
		long time = 0;
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(new LoanRequestReceivedEvent("1", time, 100, 5000));
		time = 1* 60 * 1000;
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(new LoanRequestReceivedEvent("2", time, 500, 4000));
		time = 10 * 60 * 1000;
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(new LoanRequestProcessedEvent("1", time, true));
		time = 20 * 60 * 1000;
		epRuntime.sendEvent(new CurrentTimeEvent(time));
		epRuntime.sendEvent(new LoanRequestProcessedEvent("2", time, false));
	}
}
