package org.bpmnwithactiviti.chapter4.loanrequest;

import org.activiti.engine.impl.pvm.delegate.ExecutionListener;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class ProcessStartedEventListener implements ExecutionListener {

	@Override
	public void notify(ExecutionListenerExecution execution) throws Exception {
		String name = (String) execution.getVariable("name");
		System.out.println("Starting loan application process for : " + name);
	}
}
