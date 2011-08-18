package org.bpmnwithactiviti.chapter8.multiinstance;

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
import org.bpmnwithactiviti.chapter10.multiinstance.DecisionVoting;
import org.bpmnwithactiviti.chapter10.multiinstance.Vote;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class MultiInstanceTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");
	
	@Test
	@Deployment(resources={"chapter8/multiinstance/multiinstance.bpmn20.xml"})
	public void doMultiTasking() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("decisionInfo", "test");
		variableMap.put("participants", "kermit,fonzie,gonzo");
		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("decisionProcess", variableMap);
		assertNotNull(processInstance);
		List<Task> taskList = activitiRule.getTaskService().createTaskQuery().list();
		assertEquals(3, taskList.size());
		int counter = 0;
		for (Task task : taskList) {
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("vote", true);
			if(counter < 2) {
				activitiRule.getTaskService().complete(task.getId(), taskMap);
			}
			counter++;
		}
		boolean voteOutcomeTested = false;
		List<HistoricDetail> historicVariableUpdateList = activitiRule.getHistoryService().createHistoricDetailQuery().variableUpdates().orderByTime().desc().list();
		for (HistoricDetail historicDetail : historicVariableUpdateList) {
			HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) historicDetail;
			if("voteOutcome".equals(historicVariableUpdate.getVariableName())) {
				DecisionVoting voting = (DecisionVoting) historicVariableUpdate.getValue();
				assertTrue(voting.isDecisionVotingOutcome());
				voteOutcomeTested = true;
				for (Vote vote : voting.getVotes()) {
					assertTrue(vote.isApproved());
				}
				break;
			}
		}
		assertTrue(voteOutcomeTested);
	}
}
