package org.bpmnwithactiviti.chapter5.parallelgateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class ParallelGatewayTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@Deployment(resources={"chapter5/parallelGateway.bpmn20.xml"})
	public void doMultiTasking() {
		ProcessInstance processInstance = activitiRule.getProcessEngine().getRuntimeService().startProcessInstanceByKey("multitaskingProcess");
		assertNotNull(processInstance);
		HistoryService historyService = activitiRule.getProcessEngine().getHistoryService();
		List<HistoricActivityInstance> historicInstanceList = historyService.createHistoricActivityInstanceQuery().list();
		assertNotNull(historicInstanceList);
		int order = 1;
		for (HistoricActivityInstance historicActivityInstance : historicInstanceList) {
			if("twitterTask".equals(historicActivityInstance.getActivityId())) {
				assertEquals(1, order);
				order++;
			} else if("facebookTask".equals(historicActivityInstance.getActivityId())) {
				assertEquals(2, order);
				order++;
			} else if("backlogEmailTask".equals(historicActivityInstance.getActivityId())) {
				assertEquals(3, order);
				order++;
			} else if("doWorkTask".equals(historicActivityInstance.getActivityId())) {
				assertEquals(4, order);
				order++;
			}
		}
	}
}
