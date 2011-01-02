package org.bpmnwithactiviti.chapter5.event;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class GossipAboutActivity implements JavaDelegate {
	
	public void gossipStart(ExecutionListenerExecution execution) {
		System.out.println("Oh my the following event took place = " + execution.getEventName());
		EventUtil.addEvent(execution, "activity");
	}

	public void gossipEnd(ExecutionListenerExecution execution) {
		System.out.println("I can gossip about process variables and execution id = " + execution.getId());
		EventUtil.addEvent(execution, "activity");
	}

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
