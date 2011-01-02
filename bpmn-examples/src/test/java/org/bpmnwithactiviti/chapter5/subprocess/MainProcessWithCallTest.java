package org.bpmnwithactiviti.chapter5.subprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MainProcessWithCallTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-job.xml");
	
	@After
	public void afterTest() {
		activitiRule.getProcessEngine().close();
	}
	
	@Test
	@Deployment(resources={"chapter5/mainProcessWithCall.bpmn20.xml", "chapter5/subProcessStandalone.bpmn20.xml"})
	public void manualWorkWithEscalation() {
		ProcessInstance processInstance = activitiRule.getProcessEngine().getRuntimeService().startProcessInstanceByKey("outsourcingProcess");
		assertNotNull(processInstance);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Assert.fail("Thread problem " + e.getMessage());
		}
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		HistoricActivityInstance historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("OutsourcingEscalation").singleResult();
		assertNotNull(historicInstance);
	}
	
	@Test
	@Deployment(resources={"chapter5/mainProcessWithCall.bpmn20.xml", "chapter5/subProcessStandalone.bpmn20.xml"})
	public void manualWorkCompleteTask() {
		ProcessInstance processInstance = activitiRule.getProcessEngine().getRuntimeService().startProcessInstanceByKey("outsourcingProcess");
		assertNotNull(processInstance);
		TaskService taskService = activitiRule.getProcessEngine().getTaskService();
		Task task = taskService.createTaskQuery().taskAssignee("contractor").singleResult();
		assertNotNull(task);
		taskService.complete(task.getId());
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		HistoricActivityInstance historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("OutsourcingEscalation").singleResult();
		assertNull(historicInstance);
	}
	
	@Test
	@Deployment(resources={"chapter5/mainProcessWithCall.bpmn20.xml", "chapter5/subProcessStandalone.bpmn20.xml"})
	public void automaticWork() {
		activitiRule.getProcessEngine().getRepositoryService().createDeployment().addClasspathResource("chapter5/subProcessStandaloneVersion2.bpmn20.xml").deploy();
		ProcessInstance processInstance = activitiRule.getProcessEngine().getRuntimeService().startProcessInstanceByKey("outsourcingProcess");
		assertNotNull(processInstance);
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		HistoricActivityInstance historicInstance = historyService.createHistoricActivityInstanceQuery().activityId("provideAnswer").singleResult();
		assertNotNull(historicInstance);
		assertEquals("scriptTask", historicInstance.getActivityType());
		assertEquals("contractorProcess:2", historicInstance.getProcessDefinitionId());
	}
}
