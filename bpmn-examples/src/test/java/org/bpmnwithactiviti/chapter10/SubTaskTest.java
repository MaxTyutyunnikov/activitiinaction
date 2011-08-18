package org.bpmnwithactiviti.chapter10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class SubTaskTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	public void createSubTasks() {
		TaskService taskService = activitiRule.getTaskService();
		Task parentTask = taskService.newTask();
		parentTask.setAssignee("kermit");
		taskService.saveTask(parentTask);
		createSubTask("fozzie", parentTask.getId());
		createSubTask("gonzo", parentTask.getId());
		List<Task> taskList = taskService.getSubTasks(parentTask.getId());
		assertEquals(2, taskList.size());
		taskService.complete(parentTask.getId());
		taskList = taskService.getSubTasks(parentTask.getId());
		assertEquals(0, taskList.size());
		List<HistoricTaskInstance> historicTaskList = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().list();
		assertEquals(3, historicTaskList.size());
		for (HistoricTaskInstance historicTaskInstance : historicTaskList) {
			assertNotNull(historicTaskInstance.getEndTime());
		}
	}
	
	private void createSubTask(String assignee, String parentTaskId) {
		TaskService taskService = activitiRule.getTaskService();
		Task subTask = taskService.newTask();
		subTask.setAssignee(assignee);
		subTask.setParentTaskId(parentTaskId);
		taskService.saveTask(subTask);
	}
}
