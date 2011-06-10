package org.bpmnwithactiviti.chapter12.bam.event;

public class LoanRequestProcessedEvent {
	
	// For correlating the events.
	private final String processInstanceId;
	private final long processedTime;
	private final boolean requestApproved;

	public LoanRequestProcessedEvent(String processInstanceId, long processedTime, boolean requestApproved) {
		this.processInstanceId = processInstanceId;
		this.processedTime = processedTime;
		this.requestApproved = requestApproved;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public long getProcessedTime() {
		return processedTime;
	}

	public boolean isRequestApproved() {
		return requestApproved;
	}
}
