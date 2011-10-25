package org.bpmnwithactiviti.chapter13.process.model;

import java.io.Serializable;

public class LoanApplicant implements Serializable {

	private static final long serialVersionUID = 1L;
	private long income;
	private String name;
	private long loanAmount;
	private String emailAddress;
	private boolean checkCreditOk;
	
	public long getIncome() {
		return income;
	}
	public void setIncome(long income) {
		this.income = income;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(long loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public boolean isCheckCreditOk() {
		return checkCreditOk;
	}
	public void setCheckCreditOk(boolean checkCreditOk) {
		this.checkCreditOk = checkCreditOk;
	}
	@Override
	public String toString() {
		return "LoanApplicant [income=" + income + ", name=" + name
				+ ", loanAmount=" + loanAmount + ", emailAddress="
				+ emailAddress + ", checkCreditOk=" + checkCreditOk + "]";
	}
}
