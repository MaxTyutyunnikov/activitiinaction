package org.bpmnwithactiviti.chapter9.model;

import java.io.Serializable;

public class LoanApplicant implements Serializable {

	private static final long serialVersionUID = 7783916729430463348L;
	private int income;
	private String name;
	private int loanAmount;
	private String emailAddress;
	
	public String toString(){
		return "LoanApplicant name = " + this.name;
	}
	
	public int getIncome() {
		return income;
	}
	public void setIncome(int income) {
		this.income = income;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(int loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
