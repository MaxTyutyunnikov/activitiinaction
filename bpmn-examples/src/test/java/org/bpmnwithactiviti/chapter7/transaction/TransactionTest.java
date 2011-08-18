package org.bpmnwithactiviti.chapter7.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.bpmnwithactiviti.chapter8.transaction.TransactionalBean;
import org.bpmnwithactiviti.common.AbstractTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter7/spring-transaction-context.xml")
public class TransactionTest extends AbstractTest {
  
  @Autowired
  private TransactionalBean transactionalBean;
  
  @Autowired
  private RuntimeService runtimeService;
  
  @Autowired
  @Rule
  public ActivitiRule activitiSpringRule;
  
	@Test
	@Deployment(resources={"chapter7/transaction/transaction.test.bpmn20.xml"})
	public void doTransactionWithCommit() throws Exception {
	  transactionalBean.execute(false);
	  ProcessInstance instance = runtimeService.createProcessInstanceQuery().singleResult();
	  Boolean throwError = (Boolean) runtimeService.getVariable(instance.getProcessInstanceId(), "throwError");
	  assertEquals(false, throwError);
	}
	
	@Test
  @Deployment(resources={"chapter7/transaction/transaction.test.bpmn20.xml"})
  public void doTransactionWithRollback() throws Exception {
	  try {
	    transactionalBean.execute(true);
	    fail("Expected an exception");
	  } catch(Exception e) {
	    // exception expected
	  }
    ProcessInstance instance = runtimeService.createProcessInstanceQuery().singleResult();
    assertNull(instance);
  }
	
}
