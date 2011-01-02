package org.bpmnwithactiviti.chapter6;

import org.activiti.engine.RuntimeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SomeBean {

	private RuntimeService runtimeService;

	@Transactional
	public void doingTransactionalStuff(boolean error){
		System.out.println("In Somebean doingTransactionalStuff, error = " + error
				+ " transaction is available = " + TransactionSynchronizationManager.isActualTransactionActive());
		runtimeService.startProcessInstanceByKey("transactionTest");
		if(error){
			throw new UnsupportedOperationException("Rollback needed!!");
		}
	}
	
	public void doingSimpleStuff(){
		System.out.println("In Somebean doingSimpleStuff");
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
}
