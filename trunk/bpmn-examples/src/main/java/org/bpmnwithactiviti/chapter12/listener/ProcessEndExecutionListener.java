package org.bpmnwithactiviti.chapter12.listener;

import java.util.Date;

import javax.xml.bind.JAXBContext;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.bpmnwithactiviti.chapter12.bam.event.LoanRequestProcessedEvent;

public class ProcessEndExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LoanRequestProcessedEvent event = new LoanRequestProcessedEvent (
			execution.getId(), 
			new Date().getTime(),
			(Boolean) execution.getVariable("requestApproved"),
			(Integer) execution.getVariable("loanAmount"));
		System.out.println(">>> Throwing event: "+event);
		JAXBContext.newInstance(event.getClass()).createMarshaller().marshal(event, System.out);
	}
}
