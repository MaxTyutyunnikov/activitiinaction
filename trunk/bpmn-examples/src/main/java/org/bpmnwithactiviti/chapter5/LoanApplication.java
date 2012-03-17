package org.bpmnwithactiviti.chapter5;

import java.io.Serializable;

public class LoanApplication implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String customerName;
	private long income;
	private long requestedAmount;
	private boolean creditCheckOk;
	private String explanation;
	private String emailAddress;
	
	public String getCustomerName() {
  	return customerName;
  }
	public void setCustomerName(String customerName) {
  	this.customerName = customerName;
  }
	public long getIncome() {
  	return income;
  }
	public void setIncome(long income) {
  	this.income = income;
  }
	public long getRequestedAmount() {
  	return requestedAmount;
  }
	public void setRequestedAmount(long requestedAmount) {
  	this.requestedAmount = requestedAmount;
  }
	public boolean isCreditCheckOk() {
  	return creditCheckOk;
  }
	public void setCreditCheckOk(boolean creditCheckOk) {
  	this.creditCheckOk = creditCheckOk;
  }
	public String getExplanation() {
  	return explanation;
  }
	public void setExplanation(String explanation) {
  	this.explanation = explanation;
  }
	public String getEmailAddress() {
  	return emailAddress;
  }
	public void setEmailAddress(String emailAddress) {
  	this.emailAddress = emailAddress;
  }
}
