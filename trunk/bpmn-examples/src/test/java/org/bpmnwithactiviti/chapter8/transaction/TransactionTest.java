package org.bpmnwithactiviti.chapter8.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.activiti.engine.HistoryService;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:chapter8/spring-transaction-context.xml")
public class TransactionTest {
  
  @Autowired
  private TransactionalBean transactionalBean;
  
  @Autowired
  private HistoryService historyService;
  
  @Autowired
  @Rule
  public ActivitiRule activitiSpringRule;
  
	@Test
	@Deployment(resources={"chapter8/transaction/transaction.test.bpmn20.xml"})
	public void doTransactionWithCommit() throws Exception {
	  transactionalBean.execute(false);
	  assertEquals(2, historyService.createHistoricProcessInstanceQuery().list().size());
	}
	
	@Test
  @Deployment(resources={"chapter8/transaction/transaction.test.bpmn20.xml"})
  public void doTransactionWithRollback() throws Exception {
	  try {
	    transactionalBean.execute(true);
	    fail("Expected an exception");
	  } catch(Exception e) {
	    // exception expected
	  }
    assertEquals(0, historyService.createHistoricProcessInstanceQuery().list().size());
  }
	
}
