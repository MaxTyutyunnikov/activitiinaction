package org.bpmnwithactiviti.chapter12;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.chapter12.model.LoanApplicant;
import org.junit.Rule;
import org.junit.Test;

public class SimpleCreditCheckTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(
			"activiti.cfg-mem-rules.xml");

	@SuppressWarnings("unchecked")
	@Test
	@Deployment(resources = { "chapter12/creditCheckRules.bpmn20.xml", "chapter12/CreditCheckTest.drl" })
	public void testCreditCheckFailed() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
		LoanApplicant piggy = new LoanApplicant();
		piggy.setName("Miss Piggy");
		piggy.setIncome(100);
		piggy.setLoanAmount(90);
		
		variableMap.put("missPiggy", piggy);
		
		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("creditCheckRuleProcess", variableMap);
		assertNotNull(processInstance);
		
		Collection<Object> ruleOutputList = (Collection<Object>) activitiRule
				.getRuntimeService().getVariable(processInstance.getId(),
						"rulesOutput");
		assertNotNull(ruleOutputList);
		
		for(Object obj : ruleOutputList){
			if(obj instanceof LoanApplicant) {
				assertFalse(((LoanApplicant) obj).isCheckCreditOk());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Deployment(resources = { "chapter12/creditCheckRules.bpmn20.xml", "chapter12/CreditCheckTest.drl" })
	public void testCreditCheckSucceeded() {
		
		LoanApplicant piggy = new LoanApplicant();
		piggy.setName("Miss Piggy");
		piggy.setIncome(100);
		piggy.setLoanAmount(30);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("missPiggy", piggy);
		
		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("creditCheckRuleProcess", variableMap);
		assertNotNull(processInstance);
		
		Collection<Object> ruleOutputList = (Collection<Object>) activitiRule
				.getRuntimeService().getVariable(processInstance.getId(),
						"rulesOutput");
		assertNotNull(ruleOutputList);
		
		for(Object obj : ruleOutputList){
			if(obj instanceof LoanApplicant) {
				assertTrue(((LoanApplicant) obj).isCheckCreditOk());
			}
		}
	}
}