package org.bpmnwithactiviti.chapter4.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class HistoryServiceTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	private String startAndComplete() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", "123456");
		String processInstanceID = runtimeService.startProcessInstanceByKey("bookorder", variableMap).getId();
		
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskCandidateGroup("sales").singleResult();
		variableMap = new HashMap<String, Object>();
		variableMap.put("extraInfo", "Extra information");
		variableMap.put("isbn", "654321");
		taskService.complete(task.getId(), variableMap);
		return processInstanceID;
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.bpmn20.xml"})
	public void queryHistoricInstances() {
		String processInstanceID = startAndComplete();
		HistoryService historyService = activitiRule.getHistoryService();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
			.processInstanceId(processInstanceID)
			.singleResult();
		assertNotNull(historicProcessInstance);
		assertEquals(processInstanceID, historicProcessInstance.getId());
		System.out.println("history process with definition id " + historicProcessInstance.getProcessDefinitionId() +
				", started at " + historicProcessInstance.getStartTime() + 
				", ended at " + historicProcessInstance.getEndTime() +
				", duration was " + historicProcessInstance.getDurationInMillis());
	}
	
	@Test
	@Deployment(resources={"chapter4/bookorder.bpmn20.xml"})
	public void queryHistoricActivities() {
		startAndComplete();
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery().list();
		assertEquals(4, activityList.size());
		for (HistoricActivityInstance historicActivityInstance : activityList) {
			assertNotNull(historicActivityInstance.getActivityId());
			System.out.println("history activity " + historicActivityInstance.getActivityName() +
					", type " + historicActivityInstance.getActivityType() +
					", starttime " + historicActivityInstance.getStartTime() +
					", endtime " + historicActivityInstance.getEndTime() +
					", duration was " + historicActivityInstance.getDurationInMillis());
		}
	}

	@Test
	@Deployment(resources={"chapter4/bookorder.bpmn20.xml"})
	public void queryHistoricVariableUpdates() {
		startAndComplete();
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricDetail> historicVariableUpdateList = historyService.createHistoricDetailQuery().variableUpdates().list();
		assertNotNull(historicVariableUpdateList);
		assertEquals(3, historicVariableUpdateList.size());
		for (HistoricDetail historicDetail : historicVariableUpdateList) {
			assertTrue(historicDetail instanceof HistoricVariableUpdate);
			HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
			assertNotNull(historicVariableUpdate.getExecutionId());
			System.out.println("historic variable update, revision " + historicVariableUpdate.getRevision() +
					", variable type name " + historicVariableUpdate.getVariableTypeName() +
					", variable name " + historicVariableUpdate.getVariableName() +
					", Variable value '" + historicVariableUpdate.getValue()+"'");
		}
	}
}
