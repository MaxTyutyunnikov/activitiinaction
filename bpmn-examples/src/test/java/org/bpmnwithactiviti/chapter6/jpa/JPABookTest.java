package org.bpmnwithactiviti.chapter6.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter6/jpa/jpa-application-context.xml")
public class JPABookTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
		
	@Test
	public void executeJavaService() {
	  
	  Map<String, Object> processVariables = new HashMap<String, Object>();
	  List<String> authorList = new ArrayList<String>();
	  authorList.add("Tijs Rademakers");
	  authorList.add("Ron van Liempd");
	  processVariables.put("authorList", authorList);
	  
		runtimeService.startProcessInstanceByKey("jpaTest", processVariables);
		
		Task task = taskService.createTaskQuery().singleResult();
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("booktitle", "Activiti in Action");
		formProperties.put("isbn", "123456");
		formService.submitTaskFormData(task.getId(), formProperties);
	  
		Book book = (Book) entityManager.createQuery("from Book b where b.title = ?")
		    .setParameter(1, "Activiti in Action")
		    .getSingleResult();
		assertNotNull(book);
		assertEquals("Activiti in Action", book.getTitle());
		assertEquals("Executable business processes in BPMN 2.0", book.getSubTitle());
		assertEquals(2, book.getAuthors().size());
		assertEquals("Tijs Rademakers", book.getAuthors().get(0));
	}

}
