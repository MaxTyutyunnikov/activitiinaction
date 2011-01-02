package org.bpmnwithactiviti.chapter4.loanrequest;

import java.io.Serializable;

public class LoanApplication implements Serializable {

	private static final long serialVersionUID = 4651949464749342472L;
	
	private String customerName;
	private int requestedAmount;
	private boolean creditCheckOk;
	private boolean riskCheckOk;
	private String motivation;
	private String emailAddres;
	
	public String getEmailAddres() {
		return emailAddres;
	}
	public void setEmailAddres(String emailAddres) {
		this.emailAddres = emailAddres;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	
	public boolean isCreditCheckOk() {
		return creditCheckOk;
	}
	public void setCreditCheckOk(boolean creditCheckOk) {
		this.creditCheckOk = creditCheckOk;
	}
	public boolean isRiskCheckOk() {
		return riskCheckOk;
	}
	public void setRiskCheckOk(boolean riskCheckOk) {
		this.riskCheckOk = riskCheckOk;
	}
	
	public int getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(int requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

}
