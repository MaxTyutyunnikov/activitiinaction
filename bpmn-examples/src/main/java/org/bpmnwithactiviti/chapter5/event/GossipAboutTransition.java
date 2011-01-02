package org.bpmnwithactiviti.chapter5.event;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class GossipAboutTransition implements JavaDelegate {
	
	public void gossip(ExecutionListenerExecution execution) {
		PvmTransition transition = (PvmTransition) execution.getEventSource();
		System.out.println("Did you hear " + transition.getSource().getId() + 
				" transitioned to " + transition.getDestination().getId());
		EventUtil.addEvent(execution, "transition");
	}

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
