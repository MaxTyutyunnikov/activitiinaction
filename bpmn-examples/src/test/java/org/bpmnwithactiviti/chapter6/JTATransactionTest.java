package org.bpmnwithactiviti.chapter6;

import static org.junit.Assert.assertEquals;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;


public class JTATransactionTest {

	@Rule 
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg-mem.xml");
	
	@Test
	@Deployment(resources={"chapter6/transaction.test.bpmn20.xml"})
	public void testJTATransaction() throws Exception {
		RuntimeService rt = activitiRule.getProcessEngine().getRuntimeService();
		System.out.println("Available processinstances in db before test: " + rt.createProcessInstanceQuery().count());
		try {
			JTABean jb = new JTABean(rt);
			jb.doingTransactionalStuff(false);
			jb.doingTransactionalStuff(true);
		} catch (RuntimeException re) {
			System.out.println("Catching RuntimeException for testing purposes.." + re.getMessage());
			assertEquals("Rollback needed!!", re.getMessage());
		}
		System.out.println("Available processinstances in db after test: " + rt.createProcessInstanceQuery().count());
		assertEquals(1, rt.createProcessInstanceQuery().count());
	}
	
}
