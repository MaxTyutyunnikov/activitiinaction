package org.bpmnwithactiviti.osgi;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;


public class TestBean implements JavaDelegate {

	@Override
  public void execute(DelegateExecution execution) throws Exception {
		System.out.println("invoked TestBean !!!!!!!!!!!!!!!!!!!!");
		Customer customer = new Customer();
		customer.setName("test");
		execution.setVariable("customer", customer);
  }

}
