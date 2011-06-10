package org.bpmnwithactiviti.chapter12.bam.event;

public class LoanRequestReceivedEvent {

	// For correlating the events.
	private final String processInstanceId;
	private final long receiveTime;
	private final int requestedAmount;
	
	public LoanRequestReceivedEvent(String processInstanceId, long receiveTime, int requestedAmount) {
		this.processInstanceId = processInstanceId;
		this.receiveTime = receiveTime;
		this.requestedAmount = requestedAmount;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public int getRequestedAmount() {
		return requestedAmount;
	}

	@Override
	public String toString() {
		return "LoanRequestReceivedEvent{processInstanceId="+processInstanceId+",receiveTime="+receiveTime+",requestedAmount="+requestedAmount+"}";
	}
}
