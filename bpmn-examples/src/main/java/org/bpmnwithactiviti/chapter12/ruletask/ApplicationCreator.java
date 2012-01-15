package org.bpmnwithactiviti.chapter12.ruletask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.bpmnwithactiviti.chapter12.model.LoanApplicant;
import org.bpmnwithactiviti.chapter12.model.LoanApplication;

public class ApplicationCreator implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		LoanApplicant applicant = new LoanApplicant();
		applicant.setName((String) execution.getVariable("name"));
		applicant.setIncome((Long) execution.getVariable("income"));
		applicant.setLoanAmount((Long) execution.getVariable("loanAmount"));
		applicant.setEmailAddress((String) execution.getVariable("emailAddress"));
		execution.setVariable("loanApplicant", applicant);
		
		LoanApplication la = new LoanApplication();
		la.setApplicant(applicant);
		execution.setVariable("loanApplication", la);
	}

}
