package org.bpmnwithactiviti.chapter8.transaction;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class TransactionalBean {
  
  @Autowired
  private RuntimeService runtimeService;

  @Transactional
	public void execute(boolean throwError) throws Exception {
  	
  	runtimeService.startProcessInstanceByKey("transactionTest");
  	
		Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("throwError", throwError);
    runtimeService.startProcessInstanceByKey("transactionTest", variableMap);
	}
}
