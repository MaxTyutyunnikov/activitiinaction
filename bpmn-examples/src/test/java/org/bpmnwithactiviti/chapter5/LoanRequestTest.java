package org.bpmnwithactiviti.chapter5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class LoanRequestTest {	
	
  @Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem-fullhistory.xml");

	@Test
	@Deployment(resources={"chapter5/loanrequest_firstpart.bpmn20.xml"})
	public void creditCheckTrue() {
		Map<String, Object> processVariables = new HashMap<String, Object>();
		processVariables.put("name", "Miss Piggy");
		processVariables.put("income", 100l);
		processVariables.put("loanAmount", 10l);
		processVariables.put("emailAddress", "miss.piggy@localhost");
		activitiRule.getRuntimeService().startProcessInstanceByKey(
		        "loanrequest", processVariables);
		
		List<HistoricDetail> historyVariables = activitiRule.getHistoryService()
		    .createHistoricDetailQuery()
		    .variableUpdates()
		    .orderByVariableName()
		    .asc()
		    .list();
		
		assertNotNull(historyVariables);
		assertEquals(7, historyVariables.size());
		HistoricVariableUpdate loanAppUpdate = ((HistoricVariableUpdate) historyVariables.get(5));
		assertEquals("loanApplication", loanAppUpdate.getVariableName());
		LoanApplication la = (LoanApplication) loanAppUpdate.getValue();
		assertEquals(true, la.isCreditCheckOk()); 
	}
}