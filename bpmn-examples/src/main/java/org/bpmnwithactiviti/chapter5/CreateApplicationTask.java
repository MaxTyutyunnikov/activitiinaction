package org.bpmnwithactiviti.chapter5;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateApplicationTask implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplication la = new LoanApplication();
		la.setCreditCheckOk((Boolean) execution.getVariable("creditCheckOk"));
		la.setCustomerName((String) execution.getVariable("name"));
		la.setIncome((Integer) execution.getVariable("income"));
		la.setRequestedAmount((Integer) execution.getVariable("loanAmount"));
		la.setEmailAddres((String) execution.getVariable("emailAddress"));
		execution.setVariable("loanApplication", la);
	}

}
