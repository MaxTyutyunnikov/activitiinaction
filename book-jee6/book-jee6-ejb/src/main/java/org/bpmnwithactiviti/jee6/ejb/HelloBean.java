package org.bpmnwithactiviti.jee6.ejb;

import javax.ejb.Stateless;

import org.bpmnwithactiviti.jee6.Hello;

@Stateless
public class HelloBean implements Hello {
	
	public void sayHello(String name) {
		System.out.println("hi " + name);
	}

}
