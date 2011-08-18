package org.bpmnwithactiviti.chapter6.listener;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class GossipAboutTransition {
	
	public void gossip(ExecutionListenerExecution execution) {
		PvmTransition transition = (PvmTransition) execution.getEventSource();
		System.out.println("Did you hear " + transition.getSource().getId() + 
				" transitioned to " + transition.getDestination().getId());
		EventUtil.addEvent(execution, "transition");
	}
}
