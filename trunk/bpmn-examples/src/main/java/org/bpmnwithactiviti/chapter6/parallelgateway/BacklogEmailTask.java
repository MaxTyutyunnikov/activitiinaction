package org.bpmnwithactiviti.chapter6.parallelgateway;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class BacklogEmailTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Working through email backlog");
		Thread.sleep(5000);
		System.out.println("Ready with email backlog");
	}

}
