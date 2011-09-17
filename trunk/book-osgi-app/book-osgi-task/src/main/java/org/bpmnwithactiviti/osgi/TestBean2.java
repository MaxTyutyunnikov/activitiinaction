package org.bpmnwithactiviti.osgi;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;


public class TestBean2 implements JavaDelegate {

	@Override
  public void execute(DelegateExecution execution) throws Exception {
		System.out.println("invoked TestBean2 !!!!!!!!!!!!!!!!!!!!");
		System.out.println("variable name = " + ((Customer) execution.getVariable("name")).getName());
  }

}
