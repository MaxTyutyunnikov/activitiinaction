package org.bpmnwithactiviti.chapter5.subprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class SubProcessTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@After
	public void afterTest() {
		activitiRule.getProcessEngine().close();
	}
	
	@Test
	@Deployment(resources={"chapter5/subProcess.bpmn20.xml"})
	public void developerProvidesSolution() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("problemDescription", "finish project A");
		RuntimeService runtimeService = activitiRule.getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("delegationProcess", variableMap);
		assertNotNull(processInstance);
		TaskService taskService = activitiRule.getProcessEngine().getTaskService();
		Task task = taskService.createTaskQuery().taskAssignee("developer").singleResult();
		assertNotNull(task);
		taskService.complete(task.getId());
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		HistoricActivityInstance historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("DeveloperEscalation").singleResult();
		assertNull(historicInstance);
	}
	
	@Test
	@Deployment(resources={"chapter5/subProcess.bpmn20.xml"})
	public void fullEscalation() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("problemDescription", "finish project A");
		RuntimeService runtimeService = activitiRule.getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("delegationProcess", variableMap);
		assertNotNull(processInstance);
		List<Job> jobList = activitiRule.getManagementService().createJobQuery().orderByJobDuedate().asc().list();
		assertEquals(2, jobList.size());
		for (Job job : jobList) {
			activitiRule.getManagementService().executeJob(job.getId());
		}
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		HistoricActivityInstance historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("DeveloperEscalation").singleResult();
		assertNotNull(historicInstance);
		historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("ManagerEscalation").singleResult();
		assertNotNull(historicInstance);
	}
	
	@Test
	@Deployment(resources={"chapter5/subProcess.bpmn20.xml"})
	public void managerProvidesSolution() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("problemDescription", "finish project A");
		RuntimeService runtimeService = activitiRule.getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("delegationProcess", variableMap);
		assertNotNull(processInstance);
		List<Job> jobList = activitiRule.getManagementService().createJobQuery().orderByJobDuedate().asc().list();
		assertEquals(2, jobList.size());
		activitiRule.getManagementService().executeJob(jobList.get(0).getId());
		TaskService taskService = activitiRule.getProcessEngine().getTaskService();
		Task task = taskService.createTaskQuery().taskAssignee("manager").singleResult();
		assertNotNull(task);
		taskService.complete(task.getId());
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		HistoricActivityInstance historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("DeveloperEscalation").singleResult();
		assertNotNull(historicInstance);
		historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("ManagerEscalation").singleResult();
		assertNull(historicInstance);
	}
}
