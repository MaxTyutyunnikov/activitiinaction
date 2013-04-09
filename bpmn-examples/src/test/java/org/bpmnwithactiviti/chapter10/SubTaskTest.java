package org.bpmnwithactiviti.chapter10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class SubTaskTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	public void completeSubTasks() {
		TaskService taskService = activitiRule.getTaskService();
		Task parentTask = taskService.newTask();
		parentTask.setAssignee("kermit");
		taskService.saveTask(parentTask);
		createSubTask("fozzie", parentTask.getId());
		createSubTask("gonzo", parentTask.getId());
		List<Task> taskList = taskService.getSubTasks(parentTask.getId());
		assertEquals(2, taskList.size());
		taskService.complete(taskList.get(0).getId());
		taskService.complete(taskList.get(1).getId());
		taskList = taskService.getSubTasks(parentTask.getId());
		assertEquals(0, taskList.size());
		List<HistoricTaskInstance> historicTaskList = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().finished().list();
		assertEquals(2, historicTaskList.size());
		taskService.complete(parentTask.getId());
		historicTaskList = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().finished().list();
		assertEquals(3, historicTaskList.size());
		cleanUpTaskHistory();
	}
	
	@Test
	public void completeSubTasksViaParentTask() {
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
		List<HistoricTaskInstance> historicTaskList = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().finished().list();
		assertEquals(3, historicTaskList.size());
		cleanUpTaskHistory();
	}
	
	private void createSubTask(String assignee, String parentTaskId) {
		TaskService taskService = activitiRule.getTaskService();
		Task subTask = taskService.newTask();
		subTask.setAssignee(assignee);
		subTask.setParentTaskId(parentTaskId);
		taskService.saveTask(subTask);
	}
	
	private void cleanUpTaskHistory() {
		List<HistoricTaskInstance> historicTaskList = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().finished().list();
		List<String> taskIds = new ArrayList<String>();
		for (HistoricTaskInstance historicTaskInstance : historicTaskList) {
			assertNotNull(historicTaskInstance.getEndTime());
			taskIds.add(historicTaskInstance.getId());
		}
		
		// clean up
		for(String taskId : taskIds) {
			activitiRule.getHistoryService().deleteHistoricTaskInstance(taskId);
		}
		
	}
}
