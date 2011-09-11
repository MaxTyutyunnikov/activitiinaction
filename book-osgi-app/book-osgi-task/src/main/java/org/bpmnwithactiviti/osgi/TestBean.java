package org.bpmnwithactiviti.osgi;

import java.util.Collections;

import org.activiti.engine.impl.bpmn.behavior.BpmnActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;


public class TestBean extends BpmnActivityBehavior implements ActivityBehavior {

	@Override
  public void execute(ActivityExecution execution) throws Exception {
		System.out.println("invoked3!!!!!!!!!!!!!!!!!!!!");
		execution.takeAll(execution.getActivity().getOutgoingTransitions(), 
				Collections.singletonList(execution));
  }

}
