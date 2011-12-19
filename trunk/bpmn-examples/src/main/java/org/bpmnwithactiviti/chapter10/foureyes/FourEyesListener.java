package org.bpmnwithactiviti.chapter10.foureyes;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.el.FixedValue;

public class FourEyesListener implements TaskListener {

	private FixedValue otherTaskId;
	private FixedValue processEngineName;

	@Override
	public void notify(DelegateTask delegateTask) {
		String name = null;
		if (processEngineName != null) {
			name = processEngineName.getExpressionText();
		} else {
			name = ProcessEngines.NAME_DEFAULT;
		}
		ProcessEngine processEngine = ProcessEngines.getProcessEngines().get(name);
		HistoryService historyService = processEngine.getHistoryService();
		HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId())
				.taskDefinitionKey(otherTaskId.getExpressionText()).singleResult();

		if (historicTask == null) {
			throw new ActivitiException("The previous task " + otherTaskId.getExpressionText() + " could not be found");
		}

		String claimer = delegateTask.getAssignee();
		String previousAssigneee = historicTask.getAssignee();

		if (claimer.equalsIgnoreCase(previousAssigneee)) {
			throw new ActivitiException("Assignee of task " + otherTaskId.getExpressionText() + " is not allowed to claim this task");
		}
	}

	public void setOtherTaskId(FixedValue otherTaskId) {
		this.otherTaskId = otherTaskId;
	}

	public void setProcessEngineName(FixedValue processEngineName) {
		this.processEngineName = processEngineName;
	}
}
