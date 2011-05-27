package org.bpmnwithactiviti.chapter10.ruletask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.bpmnwithactiviti.chapter10.model.LoanApplication;

public class ManagerCheckUpdate implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplication la = (LoanApplication) execution.getVariable("loanApplication");
		if((Boolean) execution.getVariable("requestApproved")) {
			la.setStatus("approved by manager");
		} else {
			la.setStatus("denied by manager");
		}
		la.setMotivation((String) execution.getVariable("motivation"));
	}

}
