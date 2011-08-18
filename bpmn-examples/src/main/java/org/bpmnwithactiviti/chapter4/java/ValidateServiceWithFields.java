package org.bpmnwithactiviti.chapter4.java;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.Expression;

public class ValidateServiceWithFields implements JavaDelegate {
	
	private Expression validatetext;
	private Expression isbn;

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("execution id " + execution.getId());
		System.out.println("received isbn " + (Long) isbn.getValue(execution));
		execution.setVariable("validatetime", new Date());
		System.out.println(validatetext.getValue(execution).toString() + execution.getVariable("validatetime"));
	}
}
