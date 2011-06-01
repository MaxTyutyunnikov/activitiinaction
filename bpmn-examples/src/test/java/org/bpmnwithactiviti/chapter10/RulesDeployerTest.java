package org.bpmnwithactiviti.chapter10;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.chapter10.model.Person;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class RulesDeployerTest extends AbstractTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(
			"activiti.cfg-mem-rules.xml");

	@SuppressWarnings("unchecked")
	@Test
	@Deployment(resources = { "chapter10/rules.bpmn20.xml",
			"chapter10/AdultCheck.drl" })
	public void testRulesDeployment() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		
		Person pete = new Person();
		pete.setName("Pete");
		pete.setAge(22);
		
		Person john = new Person();
		john.setName("John");
		john.setAge(17);
		
		variableMap.put("pete", pete);
		variableMap.put("john", john);

		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("rulesDeployment", variableMap);
		assertNotNull(processInstance);
		
		Map<String, Object> procVars = activitiRule.getRuntimeService().getVariables(processInstance.getProcessInstanceId());
		
		Collection<Object> ruleOutputList = (Collection<Object>) activitiRule
				.getRuntimeService().getVariable(processInstance.getId(),
						"rulesOutput");
		assertNotNull(ruleOutputList);
		assertEquals(2, ruleOutputList.size());
		for(Object p : ruleOutputList){
			if(p instanceof Person && ((Person) p).getName().equals("Pete")){
				assertTrue(((Person) p).isAdult());
			}else if(p instanceof Person && ((Person) p).getName().equals("John")){
				assertFalse(((Person) p).isAdult());
			}
		}
	}
}