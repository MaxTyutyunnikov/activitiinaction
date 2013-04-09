package org.bpmnwithactiviti.chapter5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class FormServiceTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	@Deployment(resources={"chapter5/startform.bpmn20.xml"})
	public void startFormSubmit() {
		ProcessDefinition definition = activitiRule.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionKey("startFormTest").singleResult();
		assertNotNull(definition);
		FormService formService = activitiRule.getFormService();
		List<FormProperty> formList = formService.getStartFormData(definition.getId()).getFormProperties();
		assertEquals(4, formList.size());
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "Miss Piggy");
		formProperties.put("emailAddress", "piggy@localhost");
		formProperties.put("income", "400");
		formProperties.put("loanAmount", "100");
		
		formService.submitStartFormData(definition.getId(), formProperties);
		List<HistoricDetail> historyVariables = activitiRule.getHistoryService()
        	.createHistoricDetailQuery()
        	.formProperties()
        	.list();

		assertNotNull(historyVariables);
		assertEquals(4, historyVariables.size());
		HistoricFormProperty formProperty = (HistoricFormProperty) historyVariables.get(0);
		assertEquals("loanAmount", formProperty.getPropertyId());
		assertEquals("100", formProperty.getPropertyValue());
		
		formProperty = (HistoricFormProperty) historyVariables.get(1);
		assertEquals("income", formProperty.getPropertyId());
		assertEquals("400", formProperty.getPropertyValue());
	}

}