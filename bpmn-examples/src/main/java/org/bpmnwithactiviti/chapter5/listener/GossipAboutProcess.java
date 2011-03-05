package org.bpmnwithactiviti.chapter5.listener;

import org.activiti.engine.impl.pvm.delegate.ExecutionListener;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class GossipAboutProcess implements ExecutionListener {
	
	@Override
	public void notify(ExecutionListenerExecution execution) throws Exception {
		System.out.println("Did you know the following process event occurred = " + execution.getEventName());
		EventUtil.addEvent(execution, "process");
	}

}
