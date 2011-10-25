package org.bpmnwithactiviti.chapter13.process.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.bpmnwithactiviti.chapter13.process.model.LoanApplicant;
import org.bpmnwithactiviti.chapter13.process.model.LoanApplication;

public class CreateApplicationVariable implements ExecutionListener {
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LoanApplication loanApplication = new LoanApplication();
		loanApplication.setApplicant(new LoanApplicant());
		execution.setVariable("loanApplication", loanApplication);
	}
}
