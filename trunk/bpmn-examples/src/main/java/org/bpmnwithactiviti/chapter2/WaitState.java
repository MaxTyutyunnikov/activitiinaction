package org.bpmnwithactiviti.chapter2;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.delegate.SignallableActivityBehavior;

public class WaitState implements SignallableActivityBehavior {

	public void execute(ActivityExecution execution) {
		
	}

	public void signal(ActivityExecution execution, String signalName,
			Object event) {
		PvmTransition transition = findTransition(execution, signalName);
		execution.take(transition);
	}

	protected PvmTransition findTransition(ActivityExecution execution,
			String signalName) {
		for (PvmTransition transition : execution.getActivity()
				.getOutgoingTransitions()) {
			if (signalName == null) {
				if (transition.getId() == null) {
					return transition;
				}
			} else {
				if (signalName.equals(transition.getId())) {
					return transition;
				}
			}
		}
		throw new RuntimeException("no transition for signalName '"
				+ signalName + "' in WaitState '"
				+ execution.getActivity().getId() + "'");
	}
}
