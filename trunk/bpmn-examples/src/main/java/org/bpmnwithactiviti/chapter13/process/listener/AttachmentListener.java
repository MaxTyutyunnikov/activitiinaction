package org.bpmnwithactiviti.chapter13.process.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class AttachmentListener implements TaskListener {
	
	@Override
  public void notify(DelegateTask delegateTask) {
		ProcessEngine processEngine = ProcessEngines.getProcessEngines().get(ProcessEngines.NAME_DEFAULT);
  }
}
