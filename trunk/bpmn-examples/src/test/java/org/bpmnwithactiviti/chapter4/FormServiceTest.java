package org.bpmnwithactiviti.chapter4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class FormServiceTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");

	@Test
	@Deployment(resources={"chapter4/startform.bpmn20.xml", "chapter4/loanRequest.form"})
	public void startFormSubmit() {
		ProcessDefinition definition = activitiRule.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionKey("startFormTest").singleResult();
		assertNotNull(definition);
		FormService formService = activitiRule.getFormService();
		StartFormData startForm = formService.getStartFormData(definition.getId());
		assertEquals("chapter4/loanRequest.form", startForm.getFormKey());
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("name", "Miss Piggy");
		formService.submitStartFormData(definition.getId(), formProperties);
		List<HistoricDetail> historyVariables = activitiRule.getHistoryService()
        	.createHistoricDetailQuery()
        	.formProperties()
        	.list();

		assertNotNull(historyVariables);
		assertEquals(1, historyVariables.size());
		HistoricFormProperty formProperty = (HistoricFormProperty) historyVariables.get(0);
		assertEquals("name", formProperty.getPropertyId());
		assertEquals("Miss Piggy", formProperty.getPropertyValue());
	}

}