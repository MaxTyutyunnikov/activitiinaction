package org.bpmnwithactiviti.chapter12.bam.event;

public class LoanRequestProcessedEvent {
	
	// For correlating the events.
	private final String processInstanceId;
	private final long processedTime;
	private final boolean requestApproved;
	private final int requestedAmount;

	public LoanRequestProcessedEvent(String processInstanceId, long processedTime, boolean requestApproved, int requestedAmount) {
		this.processInstanceId = processInstanceId;
		this.processedTime = processedTime;
		this.requestApproved = requestApproved;
		this.requestedAmount = requestedAmount;
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
	
	public int getRequestedAmount() {
		return requestedAmount;
	}

	@Override
	public String toString() {
		return "LoanRequestProcessedEvent{processInstanceId="+processInstanceId+",processedTime="+processedTime+",requestApproved="+requestApproved+",requestedAmount="+requestedAmount+"}";
	}
}
