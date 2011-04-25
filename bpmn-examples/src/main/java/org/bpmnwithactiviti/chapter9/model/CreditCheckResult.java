package org.bpmnwithactiviti.chapter9.model;

import java.io.Serializable;

public class CreditCheckResult implements Serializable {

	private static final long serialVersionUID = 2977619690106360786L;
	private boolean creditCheckPassed;

	public boolean isCreditCheckPassed() {
		return creditCheckPassed;
	}

	public void setCreditCheckPassed(boolean creditCheckPassed) {
		this.creditCheckPassed = creditCheckPassed;
	}

}
