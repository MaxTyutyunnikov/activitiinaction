package org.bpmnwithactiviti.chapter5.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter5/gossip-application-context.xml")
public class ProcessEventListenerTest extends AbstractTest {
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
	@SuppressWarnings("unchecked")
	@Test
	public void gossip() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("gossipProcess");
		assertNotNull(processInstance);
		Task task = taskService.createTaskQuery().taskAssignee("John").singleResult();
		taskService.complete(task.getId());
		List<HistoricDetail> historyList = historyService.createHistoricDetailQuery().variableUpdates().list();
		for(int i = 0; i < historyList.size(); i++) {
			HistoricVariableUpdate variableUpdate = (HistoricVariableUpdate) historyList.get(i);
			if(variableUpdate.getValue() instanceof Boolean) continue;
			List<String> variableList = (List<String>) variableUpdate.getValue();
			assertEquals("eventList", variableUpdate.getVariableName());
			if(i == 0) {
				assertEquals("process:start", variableList.get(0));
			} else if(i == 1) {
				assertEquals("transition:take", variableList.get(1));
			} else if(i == 2) {
				assertEquals("activity:start", variableList.get(2));
			} else if(i == (historyList.size() - 1)) {
				assertEquals("process:end", variableList.get(variableList.size() - 1));
			}
		}
	}
}
