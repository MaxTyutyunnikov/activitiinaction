package org.bpmnwithactiviti.chapter4;

import java.io.Serializable;

public class LoanApplication implements Serializable {

	private static final long serialVersionUID = 4651949464749342472L;
	
	private String customerName;
	private int income;
	private int requestedAmount;
	private boolean creditCheckOk;
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
  public int getIncome() {
    return income;
  }
  public void setIncome(int income) {
    this.income = income;
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
	public int getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(int requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

}
