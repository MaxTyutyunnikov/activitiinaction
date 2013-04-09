package org.bpmnwithactiviti.chapter8.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RestTest {
	
	@Test
	public void restAPITest() throws Exception {
		String vacationProcessDefinitionId = ActivitiRestClient.getVacationRequestProcessId();
		assertNotNull(vacationProcessDefinitionId);
		String processInstanceId = ActivitiRestClient.startVacationRequestProcess(vacationProcessDefinitionId);
		assertNotNull(processInstanceId);
		String taskId = ActivitiRestClient.getHandleVacationRequestTask(processInstanceId);
		assertNotNull(taskId);
		assertEquals("true", ActivitiRestClient.claimHandleVacationRequestTask(taskId));
		assertEquals(0, ActivitiRestClient.getTasks("candidate","kermit"));
		assertEquals(1, ActivitiRestClient.getTasks("assignee","kermit"));
		assertEquals("true", ActivitiRestClient.completeHandleVacationRequestTask(taskId));
		assertEquals(0, ActivitiRestClient.getTasks("assignee","kermit"));
	}

}