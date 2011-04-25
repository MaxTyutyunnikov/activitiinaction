package org.bpmnwithactiviti.chapter9;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ResultDisplayer implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplication la = (LoanApplication) execution.getVariable("loanApplication");
		System.out.println("Status of the application = " + la.getStatus());
		execution.setVariable("loanApplication", la);
	}

}
