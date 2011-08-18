package org.bpmnwithactiviti.chapter12.model;

import java.io.Serializable;

public class LoanApplication implements Serializable {

	private static final long serialVersionUID = 1L;

	private LoanApplicant applicant;
	private String motivation;
	private String status;
	
	public LoanApplicant getApplicant() {
		return applicant;
	}

	public void setApplicant(LoanApplicant applicant) {
		this.applicant = applicant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	@Override
	public String toString() {
		return "LoanApplication [applicant=" + applicant + ", motivation="
			+ motivation + ", status=" + status + "]";
	}
}
