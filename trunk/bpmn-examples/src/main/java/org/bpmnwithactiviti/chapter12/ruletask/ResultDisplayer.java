package org.bpmnwithactiviti.chapter12.ruletask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.bpmnwithactiviti.chapter12.model.LoanApplication;

public class ResultDisplayer implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplication la = (LoanApplication) execution.getVariable("loanApplication");
		System.out.println(">>> LoanApplication = " + la);
	}
}
