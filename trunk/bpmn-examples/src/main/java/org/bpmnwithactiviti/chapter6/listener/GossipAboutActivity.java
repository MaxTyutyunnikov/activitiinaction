package org.bpmnwithactiviti.chapter6.listener;

import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class GossipAboutActivity {
	
	public void gossipStart(ExecutionListenerExecution execution) {
		System.out.println("Oh my the following event took place = " + execution.getEventName());
		EventUtil.addEvent(execution, "activity");
	}

	public void gossipEnd(ExecutionListenerExecution execution) {
		System.out.println("I can gossip about process variables and execution id = " + execution.getId());
		EventUtil.addEvent(execution, "activity");
	}

}
