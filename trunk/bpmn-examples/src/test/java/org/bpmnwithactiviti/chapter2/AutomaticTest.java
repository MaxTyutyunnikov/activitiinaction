package org.bpmnwithactiviti.chapter2;

import static org.junit.Assert.assertNotNull;

import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmProcessInstance;
import org.junit.Test;

public class AutomaticTest {

	@Test
	public void testAutomatic() {
		PvmProcessDefinition processDefinition = new ProcessDefinitionBuilder()
				.createActivity("A")
					.initial()
					.behavior(new AutomaticActivity())
					.transition("B")
					.endActivity()
				.createActivity("B")
					.behavior(new AutomaticActivity())
					.transition("C")
					.endActivity()
				.createActivity("C")
					.behavior(new WaitState())
					.endActivity()
				.buildProcessDefinition();

		PvmProcessInstance processInstance = processDefinition
				.createProcessInstance();
		processInstance.start();

		assertNotNull(processInstance.findExecution("C"));
	}

}
