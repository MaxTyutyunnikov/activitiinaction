package org.bpmnwithactiviti.chapter14.test;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestProcessedEvent;

import com.espertech.esper.client.EPServiceProviderManager;

public class ProcessEndExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LoanRequestProcessedEvent event = new LoanRequestProcessedEvent (
			execution.getId(), 
			new Date().getTime(),
			(Boolean) execution.getVariable("requestApproved"),
			(Integer) execution.getVariable("loanAmount"));
		System.out.println(">>> Throwing event: "+event);
		EPServiceProviderManager.getDefaultProvider().getEPRuntime()
			.getEventSender("LoanRequestProcessedEvent")
			.sendEvent(event);
	}
}
