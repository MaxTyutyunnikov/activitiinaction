package org.bpmnwithactiviti.chapter5.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter5/jpa-application-context.xml")
public class JPABookTest extends AbstractTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
		
	@Test
	@Transactional(value="jpaTransactionManager")
	public void executeJavaService() {
		runtimeService.startProcessInstanceByKey("jpaTest");
		
		Task task = taskService.createTaskQuery().singleResult();
	    Map<String, String> formProperties = new HashMap<String, String>();
	    formProperties.put("booktitle", "Activiti in Action");
	    formProperties.put("isbn", "123456");
	    formService.submitTaskFormData(task.getId(), formProperties);
		
		Book book = entityManager.find(Book.class, 1);
		assertNotNull(book);
		assertEquals("Activiti in Action", book.getTitle());
		assertEquals("Executable business processes in BPMN 2.0", book.getSubTitle());
	}

}
