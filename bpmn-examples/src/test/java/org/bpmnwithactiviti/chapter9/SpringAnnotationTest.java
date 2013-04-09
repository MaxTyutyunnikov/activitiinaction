package org.bpmnwithactiviti.chapter9;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter9/spring-application-context.xml")
public class SpringAnnotationTest {

	@Autowired
	ProcessInitiator initiator;
	
	@Test
	public void simpleProcessTest() {
		String instanceID = initiator.startBookOrder("123456", 3);
		assertNotNull(instanceID);
	}
}
