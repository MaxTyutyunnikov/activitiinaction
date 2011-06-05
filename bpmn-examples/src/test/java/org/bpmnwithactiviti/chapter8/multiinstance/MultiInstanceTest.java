package org.bpmnwithactiviti.chapter8.multiinstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;

public class MultiInstanceTest extends AbstractTest {
	
	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@Test
	@Deployment(resources={"chapter8/multiinstance/multiinstance.bpmn20.xml"})
	public void doMultiTasking() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("decisionInfo", "test");
		variableMap.put("participants", "kermit,fonzie,gonzo");
		ProcessInstance processInstance = activitiRule.getRuntimeService()
				.startProcessInstanceByKey("decisionProcess");
		assertNotNull(processInstance);
		List<Task> taskList = activitiRule.getTaskService().createTaskQuery().list();
		assertEquals(2, taskList.size());
		variableMap = activitiRule.getRuntimeService().getVariables(processInstance.getProcessInstanceId());
		Set<String> variableNameSet = variableMap.keySet();
		for(String variableName : variableNameSet) {
			System.out.println("name " + variableName + ", value " + variableMap.get(variableName));
		}
	}
}
