package org.bpmnwithactiviti.chapter9.camel;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter9/camel/application-context.xml")
public class CamelTest extends AbstractTest {

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	@Rule
	public ActivitiRule activitiSpringRule;

	@Test
	public void simpleProcessTest() {
    ProducerTemplate tpl = camelContext.createProducerTemplate();
    String instanceId = (String) tpl.requestBody("direct:start", Collections.singletonMap("isbn", "1234"));
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("Complete order", task.getName());
		taskService.complete(task.getId());
	}
}
