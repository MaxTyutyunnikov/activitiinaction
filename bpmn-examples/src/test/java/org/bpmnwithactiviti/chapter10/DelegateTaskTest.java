package org.bpmnwithactiviti.chapter10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class DelegateTaskTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	public void delegateTask() {
		TaskService taskService = activitiRule.getTaskService();
		Task delegateTask = taskService.newTask();
		delegateTask.setOwner("kermit");
		taskService.saveTask(delegateTask);
		Task queryTask = taskService.createTaskQuery().singleResult();
		assertEquals("kermit", queryTask.getOwner());
		assertNull(queryTask.getAssignee());
		taskService.delegateTask(delegateTask.getId(), "fonzie");
		queryTask = taskService.createTaskQuery().singleResult();
		assertEquals("fonzie", queryTask.getAssignee());
		assertEquals(DelegationState.PENDING, queryTask.getDelegationState());
		taskService.resolveTask(delegateTask.getId());
		queryTask = taskService.createTaskQuery().singleResult();
		assertEquals("kermit", queryTask.getAssignee());
		assertEquals(DelegationState.RESOLVED, queryTask.getDelegationState());
		taskService.complete(delegateTask.getId());
		List<HistoricTaskInstance> historicTaskList = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().list();
		assertEquals(1, historicTaskList.size());
		for (HistoricTaskInstance historicTaskInstance : historicTaskList) {
			assertNotNull(historicTaskInstance.getEndTime());
		}
	}
}
