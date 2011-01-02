package org.bpmnwithactiviti.chapter6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bpmnwithactiviti.chapter6.ActivitiRestClient;
import org.junit.Test;

public class RestTest {
	
	@Test
	public void restAPITest() throws Exception {
		String vacationProcessDefinitionId = ActivitiRestClient.getVacationRequestProcessId();
		assertEquals("vacationRequest:1", vacationProcessDefinitionId);
		String processInstanceId = ActivitiRestClient.startVacationRequestProcess(vacationProcessDefinitionId);
		assertNotNull(processInstanceId);
		assertEquals(1, ActivitiRestClient.getTasks("candidate","kermit"));
		String taskId = ActivitiRestClient.getHandleVacationRequestTask(processInstanceId);
		assertNotNull(taskId);
		assertEquals("true", ActivitiRestClient.claimHandleVacationRequestTask(taskId));
		assertEquals(0, ActivitiRestClient.getTasks("candidate","kermit"));
		assertEquals(1, ActivitiRestClient.getTasks("assignee","kermit"));
		assertEquals("true", ActivitiRestClient.completeHandleVacationRequestTask(taskId));
		assertEquals(0, ActivitiRestClient.getTasks("assignee","kermit"));
	}

}