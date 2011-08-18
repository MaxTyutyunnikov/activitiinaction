package org.bpmnwithactiviti.chapter14.bam.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "loanRequestReceivedEvent")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanRequestReceivedEvent {

	// For correlating the events.
	@XmlElement
	private final String processInstanceId;
	@XmlElement
	private final long receiveTime;
	@XmlElement
	private final int requestedAmount;
	
	@SuppressWarnings("unused")
	private LoanRequestReceivedEvent() {
		processInstanceId = null;
		receiveTime = 0L;
		requestedAmount = 0;
	}
	
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
