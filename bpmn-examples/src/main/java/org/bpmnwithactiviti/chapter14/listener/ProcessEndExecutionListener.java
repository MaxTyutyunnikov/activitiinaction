package org.bpmnwithactiviti.chapter14.listener;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.bpmnwithactiviti.chapter14.bam.event.LoanRequestProcessedEvent;

public class ProcessEndExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LoanRequestProcessedEvent event = new LoanRequestProcessedEvent (
			execution.getId(), 
			new Date().getTime(),
			(Boolean) execution.getVariable("requestApproved"),
			(Integer) execution.getVariable("loanAmount"));
		EventSender.send(event);
	}
}
