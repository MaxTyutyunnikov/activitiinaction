package org.bpmnwithactiviti.chapter4.spring;

import org.activiti.engine.delegate.DelegateExecution;

public class OrderService {
	
	public void validate(DelegateExecution execution) {
		System.out.println("validating order for isbn " + execution.getVariable("isbn"));
	}

}
