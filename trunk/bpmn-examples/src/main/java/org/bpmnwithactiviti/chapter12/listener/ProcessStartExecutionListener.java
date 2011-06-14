package org.bpmnwithactiviti.chapter12.listener;

import java.util.Date;

import javax.xml.bind.JAXBContext;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.bpmnwithactiviti.chapter12.bam.event.LoanRequestReceivedEvent;

public class ProcessStartExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LoanRequestReceivedEvent event = new LoanRequestReceivedEvent(
			execution.getId(), 
			new Date().getTime(), 
			(Integer) execution.getVariable("loanAmount"));
		System.out.println(">>> Throwing event: "+event);
		JAXBContext.newInstance(event.getClass()).createMarshaller().marshal(event, System.out);
	}
}
