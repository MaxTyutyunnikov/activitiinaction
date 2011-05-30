package org.bpmnwithactiviti.chapter10;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.chapter10.model.CreditCheckResult;
import org.bpmnwithactiviti.chapter10.model.LoanApplicant;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class SimpleCreditCheckTest extends AbstractTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(
			"activiti.cfg-mem-rules.xml");

	@SuppressWarnings("unchecked")
	@Test
	@Deployment(resources = { "chapter10/creditCheckRules.bpmn20.xml", "chapter10/CreditCheckTest.drl" })
	public void testCreditCheckFailed() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
		LoanApplicant piggy = new LoanApplicant();
		piggy.setName("Miss Piggy");
		piggy.setIncome(100);
		piggy.setLoanAmount(90);
		
		variableMap.put("missPiggy", piggy);
		variableMap.put("CreditCheckResult", new CreditCheckResult());

		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("creditCheckRuleProcess", variableMap);
		assertNotNull(processInstance);
		assertTrue(processInstance.getProcessDefinitionId().startsWith(
				"creditCheckRuleProcess:1"));

		Collection<Object> ruleOutputList = (Collection<Object>) activitiRule
				.getRuntimeService().getVariable(processInstance.getId(),
						"rulesOutput");
		assertNotNull(ruleOutputList);
		
		for(Object obj : ruleOutputList){
			if(obj instanceof CreditCheckResult){
				assertFalse(((CreditCheckResult) obj).isCreditCheckPassed());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Deployment(resources = { "chapter10/creditCheckRules.bpmn20.xml", "chapter10/CreditCheckTest.drl" })
	public void testCreditCheckSucceeded() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
		LoanApplicant piggy = new LoanApplicant();
		piggy.setName("Miss Piggy");
		piggy.setIncome(100);
		piggy.setLoanAmount(40);
		
		variableMap.put("missPiggy", piggy);
		variableMap.put("CreditCheckResult", new CreditCheckResult());

		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("creditCheckRuleProcess", variableMap);
		assertNotNull(processInstance);
		assertTrue(processInstance.getProcessDefinitionId().startsWith(
				"creditCheckRuleProcess:1"));

		Collection<Object> ruleOutputList = (Collection<Object>) activitiRule
				.getRuntimeService().getVariable(processInstance.getId(),
						"rulesOutput");
		assertNotNull(ruleOutputList);
		
		for(Object obj : ruleOutputList){
			if(obj instanceof CreditCheckResult){
				assertTrue(((CreditCheckResult) obj).isCreditCheckPassed());
			}
		}
	}
}