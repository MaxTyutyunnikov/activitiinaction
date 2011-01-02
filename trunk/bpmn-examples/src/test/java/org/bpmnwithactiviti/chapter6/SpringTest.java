package org.bpmnwithactiviti.chapter6;

import static org.junit.Assert.assertEquals;

import org.activiti.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter6/spring-transaction-test-application-context.xml")
public class SpringTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private SomeBean someBean;

	@Test
	public void simpleProcessTest() {
		System.out.println("Available processinstances in db before test: " + runtimeService.createProcessInstanceQuery().count());
		try {
			someBean.doingTransactionalStuff(false);
			someBean.doingTransactionalStuff(true);
		} catch (RuntimeException re) {
			System.out.println("Catching RuntimeException for testing purposes.." + re.getMessage());
			assertEquals("Rollback needed!!", re.getMessage());
		}
		System.out.println("Available processinstances in db after test: " + runtimeService.createProcessInstanceQuery().count());
		assertEquals(1, runtimeService.createProcessInstanceQuery().count());
	}
}
