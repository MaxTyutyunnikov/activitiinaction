package org.bpmnwithactiviti.chapter4.loanrequest;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ApplicationCreator implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplication la = new LoanApplication();
		la.setCreditCheckOk((Boolean) execution.getVariable("creditCheckOk"));
		la.setRiskCheckOk((Boolean) execution.getVariable("riskCheckOk"));
		la.setCustomerName((String) execution.getVariable("name"));
		la.setRequestedAmount((Integer) execution.getVariable("loanAmount"));
		la.setEmailAddres((String) execution.getVariable("emailAddress"));
		execution.setVariable("loanApplication", la);
	}

}
