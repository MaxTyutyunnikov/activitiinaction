package org.bpmnwithactiviti.chapter13.process.task;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.poi.ss.usermodel.Cell;
import org.bpmnwithactiviti.chapter13.process.model.LoanApplication;

public class EvaluationCMISTask implements JavaDelegate {
	
	@Override
  public void execute(DelegateExecution execution) throws Exception {
		LoanApplication loanApplication = (LoanApplication) execution.getVariable("loanApplication");
		
		POICMISHelper helper = new POICMISHelper();
		helper.openWorkbook("workspace://SpacesStore/c70bab92-ce68-444d-8a6c-2f0c43859e0c");
    
		boolean creditCheck = loanApplication.getApplicant().isCheckCreditOk();
		long loanAmount = loanApplication.getApplicant().getLoanAmount();
		
		boolean foundMatch = false;
		boolean reachedEndOfRules = false;
		int rowCounter = 1;
		while(foundMatch == false && reachedEndOfRules == false) {
			Cell cell = helper.getCell(rowCounter, 0);
			if(cell == null) {
				reachedEndOfRules = true;
				
			} else if(creditCheck == helper.getBooleanCellValue(rowCounter, 0)) {
				String loanAmountRule = helper.getStringCellValue(rowCounter, 1);
				if("N/A".equalsIgnoreCase(loanAmountRule)) {
					foundMatch = true;
				} else {
					int spaceIndex = loanAmountRule.indexOf(" ");
					String loanAmountRuleCompare = loanAmountRule.substring(0, spaceIndex);
					String loanAmountRuleValue = loanAmountRule.substring(spaceIndex + 1, loanAmountRule.length());
					if("<".equals(loanAmountRuleCompare)) {
						if(loanAmount < Long.valueOf(loanAmountRuleValue)) {
							foundMatch = true;
						}
					} else if("<=".equals(loanAmountRuleCompare)) {
						if(loanAmount <= Long.valueOf(loanAmountRuleValue)) {
							foundMatch = true;
						}
					} else if("=".equals(loanAmountRuleCompare)) {
						if(loanAmount == Long.valueOf(loanAmountRuleValue).longValue()) {
							foundMatch = true;
						}
					} else if(">".equals(loanAmountRuleCompare)) {
						if(loanAmount > Long.valueOf(loanAmountRuleValue)) {
							foundMatch = true;
						}
					} else if(">=".equals(loanAmountRuleCompare)) {
						if(loanAmount > Long.valueOf(loanAmountRuleValue)) {
							foundMatch = true;
						}
					}
				}
			}
			
			if(foundMatch == false) {
				rowCounter++;
			}
		}
		
		if(foundMatch == false) {
			throw new ActivitiException("No match found in decision table");
		}
		
    loanApplication.setStatus(helper.getStringCellValue(rowCounter, 2));
    
    execution.setVariable("loanApplication", loanApplication);
	}
}
