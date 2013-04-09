package org.bpmnwithactiviti.chapter4.spring;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter4/spring-test-application-context.xml")
public class SpringTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void simpleProcessTest() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", 123456L);
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("Complete order", task.getName());
		taskService.complete(task.getId());
		assertEquals(0, runtimeService.createProcessInstanceQuery().count());
	}
}
