package org.bpmnwithactiviti.chapter10.subtask;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.task.Task;

public class SubTaskListener implements TaskListener {
	
	private Expression subTaskList;

	@Override
  public void notify(DelegateTask delegateTask) {
		ProcessEngine processEngine = ProcessEngines.getProcessEngines().get(ProcessEngines.NAME_DEFAULT);
	  @SuppressWarnings("unchecked")
    List<String> subTaskNames = (List<String>) subTaskList.getValue(delegateTask.getExecution());
	  for(String subTaskName : subTaskNames) {
	  	TaskService taskService = processEngine.getTaskService();
	  	Task subTask = taskService.newTask();
	  	subTask.setName(subTaskName);
	  	subTask.setAssignee("kermit");
	  	subTask.setParentTaskId(delegateTask.getId());
	  	taskService.saveTask(subTask);
	  }
  }

	public void setSubTaskList(Expression subTaskList) {
  	this.subTaskList = subTaskList;
  }
}
