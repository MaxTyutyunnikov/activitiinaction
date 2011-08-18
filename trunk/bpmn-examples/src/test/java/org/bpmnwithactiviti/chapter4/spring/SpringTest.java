package org.bpmnwithactiviti.chapter4.spring;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter3/spring-test-application-context.xml")
public class SpringTest extends AbstractTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	@Rule
	public ActivitiRule activitiSpringRule;

	@Test
	public void simpleProcessTest() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("isbn", Long.valueOf("123456"));
		runtimeService.startProcessInstanceByKey("bookorder", variableMap);
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("Complete order", task.getName());
		taskService.complete(task.getId());
		assertEquals(0, runtimeService.createProcessInstanceQuery().count());
	}
}
