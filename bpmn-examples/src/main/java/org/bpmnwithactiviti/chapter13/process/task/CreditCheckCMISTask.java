package org.bpmnwithactiviti.chapter13.process.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.chemistry.opencmis.client.api.Document;
import org.bpmnwithactiviti.chapter13.process.model.LoanApplication;

public class CreditCheckCMISTask implements JavaDelegate {
	
	private static final String EXCEL_MIMETYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	@Override
  public void execute(DelegateExecution execution) throws Exception {
		LoanApplication loanApplication = (LoanApplication) execution.getVariable("loanApplication");
		
		POICMISHelper helper = new POICMISHelper();
		helper.openWorkbook("workspace://SpacesStore/a5715b04-7422-4e8c-bb8f-def83031103a");
    
    // Set customer name
		helper.setCellValue(loanApplication.getApplicant().getName(), 0, 1, true);
    
    // Set email address
		helper.setCellValue(loanApplication.getApplicant().getEmailAddress(), 1, 1, true);
    
    // Set income cell
		helper.setCellValue(loanApplication.getApplicant().getIncome(), 4, 0, false);
    
    // Set loan amount cell
    helper.setCellValue(loanApplication.getApplicant().getLoanAmount(), 4, 1, false);
    
    // Evaluate loan amount double formula cell
    helper.evaluateFormulaCell(4, 2);
    
    // Get credit check formula cell
    helper.evaluateFormulaCell(6, 1);
    loanApplication.getApplicant().setCheckCreditOk(helper.getBooleanCellValue(6, 1));
    
    helper.recalculateSheetAfterOpening();
    
    Document document = helper.saveWorkbookToFolder(
    		loanApplication.getApplicant().getName(), ".xls", EXCEL_MIMETYPE);
    
    helper.attachDocumentToProcess(execution.getProcessInstanceId(), document, "xls", 
    		"Credit check sheet for " + loanApplication.getApplicant().getName());
    
    execution.setVariable("loanApplication", loanApplication);
    execution.setVariable("documentFolderId", helper.documentFolder.getId());
	}
}
