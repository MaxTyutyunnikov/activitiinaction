package org.bpmnwithactiviti.chapter5;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateApplicationTask implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplication la = new LoanApplication();
		la.setCreditCheckOk((Boolean) execution.getVariable("creditCheckOk"));
		la.setCustomerName((String) execution.getVariable("name"));
		la.setIncome((Long) execution.getVariable("income"));
		la.setRequestedAmount((Long) execution.getVariable("loanAmount"));
		la.setEmailAddress((String) execution.getVariable("emailAddress"));
		execution.setVariable("loanApplication", la);
	}
}
