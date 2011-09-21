package org.bpmnwithactiviti.chapter9;

import org.activiti.spring.annotations.ProcessVariable;
import org.activiti.spring.annotations.StartProcess;

public class ProcessInitiator {
	
	@StartProcess(processKey="bookorder", returnProcessInstanceId=true)
	public String startBookOrder(@ProcessVariable("isbn") String isbn, 
			@ProcessVariable("amount") int amount) {
		return null;
	}

}
