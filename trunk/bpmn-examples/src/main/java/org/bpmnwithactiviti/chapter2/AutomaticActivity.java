package org.bpmnwithactiviti.chapter2;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class AutomaticActivity implements ActivityBehavior {

  public void execute(ActivityExecution activityExecution) throws Exception {
    List<PvmTransition> outgoingTransitions = activityExecution.getActivity().getOutgoingTransitions();
    List<ActivityExecution> executionList = new ArrayList<ActivityExecution>();
    executionList.add(activityExecution);
    activityExecution.takeAll(outgoingTransitions, executionList);
  }
}
