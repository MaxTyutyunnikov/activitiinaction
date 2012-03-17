package org.bpmnwithactiviti.jee6.task;

import javax.ejb.EJB;
import javax.inject.Named;

import org.bpmnwithactiviti.jee6.Hello;

@Named
public class HelloDelegate {

	@EJB(lookup="java:global/book-jee6-ejb/HelloBean")
	private Hello helloBean;
	
  public void sayHello(String name) {
		helloBean.sayHello(name);
  }

}
