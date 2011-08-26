package org.bpmnwithactiviti.chapter6.listener;

import org.activiti.engine.delegate.DelegateExecution;

public class GossipAboutActivity {
	
	public void gossipStart(DelegateExecution execution) {
		System.out.println("Oh my the following event took place = " + execution.getEventName());
		EventUtil.addEvent(execution, "activity");
	}

	public void gossipEnd(DelegateExecution execution) {
		System.out.println("I can gossip about process variables and execution id = " + execution.getId());
		EventUtil.addEvent(execution, "activity");
	}

}
