package org.bpmnwithactiviti.chapter8.transaction;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ErrorServiceTask implements JavaDelegate {

	@Override
  public void execute(DelegateExecution execution) throws Exception {
	  if(execution.hasVariable("throwError") && 
	  		Boolean.valueOf(execution.getVariable("throwError").toString())) {
	  	
	  	System.out.println("Throw error");
	  	throw new IllegalArgumentException("Rollback!!");
	  }
	  
	  System.out.println("No error thrown");
  }

}
