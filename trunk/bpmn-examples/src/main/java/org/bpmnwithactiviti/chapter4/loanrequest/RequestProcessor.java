package org.bpmnwithactiviti.chapter4.loanrequest;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class RequestProcessor implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		System.out.println("Processing..");
	}

}
