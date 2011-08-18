package org.bpmnwithactiviti.chapter6.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class GossipAboutProcess implements ExecutionListener {
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("Did you know the following process event occurred = " + execution.getEventName());
		EventUtil.addEvent(execution, "process");
	}

}
