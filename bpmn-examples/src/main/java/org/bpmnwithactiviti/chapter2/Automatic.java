package org.bpmnwithactiviti.chapter2;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class Automatic implements ActivityBehavior {

  public void execute(ActivityExecution activityContext) throws Exception {
    PvmTransition defaultOutgoingTransition = activityContext.getActivity().getOutgoingTransitions().get(0);
    activityContext.take(defaultOutgoingTransition);
  }
}
