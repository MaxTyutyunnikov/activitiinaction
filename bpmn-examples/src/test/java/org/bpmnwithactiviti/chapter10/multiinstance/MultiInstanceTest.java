package org.bpmnwithactiviti.chapter10.multiinstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class MultiInstanceTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	@Test
	@Deployment(resources={"chapter10/multiinstance/multiinstance.bpmn20.xml"})
	public void doMultiTasking() {
		String processDefinitionId = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult().getId();
		Map<String, String> variableMap = new HashMap<String, String>();
		variableMap.put("decisionInfo", "test");
		variableMap.put("participants", "kermit,fonzie,gonzo");
		ProcessInstance processInstance = activitiRule.getFormService()
				.submitStartFormData(processDefinitionId, variableMap);
		assertNotNull(processInstance);
		List<Task> taskList = activitiRule.getTaskService().createTaskQuery().list();
		assertEquals(3, taskList.size());
		for (Task task : taskList) {
			if (activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).count() > 0 ) {
				Map<String, String> taskMap = new HashMap<String, String>();
				taskMap.put("vote", "true");
				activitiRule.getFormService().submitTaskFormData(task.getId(), taskMap);
			}
		}
		
		boolean voteOutcomeTested = false;
		List<HistoricDetail> historicVariableUpdateList = activitiRule.getHistoryService().createHistoricDetailQuery().variableUpdates().orderByTime().desc().list();
		for (HistoricDetail historicDetail : historicVariableUpdateList) {
			HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
			if("voteOutcome".equals(historicVariableUpdate.getVariableName())) {
				voteOutcomeTested = true;
				DecisionVoting voting = (DecisionVoting) historicVariableUpdate.getValue();
				assertTrue(voting.isDecisionVotingOutcome());
				assertEquals(2, voting.getVotes().size());
				for (Vote vote : voting.getVotes()) {
					assertTrue(vote.isApproved());
				}
				break;
			}
		}
		assertTrue(voteOutcomeTested);
	}
}
