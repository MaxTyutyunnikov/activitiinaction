package org.bpmnwithactiviti.jee6.task;

import javax.ejb.EJB;
import javax.inject.Named;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

@Named
public class HelloDelegate implements JavaDelegate {

	@EJB(lookup="java:global/book-jee6-ejb/HelloBean")
	private Hello helloBean;
	
	@Override
  public void execute(DelegateExecution execution) throws Exception {
		helloBean.sayHello((String) execution.getVariable("name"));
  }

}
