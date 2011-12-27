package org.bpmnwithactiviti.chapter14.test;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestReceivedEvent;

import com.espertech.esper.client.EPServiceProviderManager;

public class ProcessStartExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LoanRequestReceivedEvent event = new LoanRequestReceivedEvent(
				execution.getId(), 
				new Date().getTime(), 
				(Integer) execution.getVariable("loanAmount"));
		System.out.println(">>> Throwing event: "+event);
		EPServiceProviderManager.getDefaultProvider().getEPRuntime()
				.getEventSender("LoanRequestReceivedEvent")
				.sendEvent(event);
	}
}
