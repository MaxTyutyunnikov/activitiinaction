package org.bpmnwithactiviti.chapter13.attachment;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class AttachmentTest {	
	
  @Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

	@Test
	public void addAttachment() throws Exception {
		activitiRule.getIdentityService().setAuthenticatedUserId("kermit");
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.newTask();
		task.setName("Task with CMIS attachments");
		task.setAssignee("kermit");
		taskService.saveTask(task);
		
		InputStream pdfStream = new FileInputStream("src/main/resources/chapter13/cmis-cheatsheet.pdf");
		taskService.createAttachment("application/pdf", task.getId(), null, "CMISCheatSheet", "CMIS cheat sheet", pdfStream);
		
		taskService.createAttachment("url", task.getId(), null, "Alfresco site", "A Alfresco site for Activiti", 
				  "http://localhost:9090/share/page/site/activiti/document-details?nodeRef=workspace://SpacesStore/007df67f-28a8-4973-a39b-459c835c0712");
		
		List<Attachment> attachmentList = taskService.getTaskAttachments(task.getId());
		assertEquals(2, attachmentList.size());
	}
}
