package org.bpmnwithactiviti.chapter12;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bpmnwithactiviti.chapter12.logic.CreditCheckDecisionTableRunner;
import org.bpmnwithactiviti.chapter12.model.LoanApplicant;
import org.junit.Test;

public class CreditCheckDecisionTableTest {
	
	@Test
	public void testCreditCheckFailed(){
		LoanApplicant piggy = new LoanApplicant();
		piggy.setName("Miss Piggy");
		piggy.setIncome(100);
		piggy.setLoanAmount(50);
		
		boolean result = false;
		try {
			 result = CreditCheckDecisionTableRunner.runRules(piggy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertFalse(result);
	}
	
	@Test
	public void testCreditCheckPassed(){
		LoanApplicant kermit = new LoanApplicant();
		kermit.setName("Kermit");
		kermit.setIncome(500);
		kermit.setLoanAmount(50);
		
		boolean result = false;
		try {
			 result = CreditCheckDecisionTableRunner.runRules(kermit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(result);
	}
	
}
