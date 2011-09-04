package org.bpmnwithactiviti.chapter14.bam.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LoanRequestProcessedEvent")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanRequestProcessedEvent {
	
	// For correlating the events.
	@XmlElement
	private final String processInstanceId;
	@XmlElement
	private final long processedTime;
	@XmlElement
	private final boolean requestApproved;
	@XmlElement
	private final int requestedAmount;
	
	@SuppressWarnings("unused")
	private LoanRequestProcessedEvent() {
		processInstanceId = null;
		processedTime = 0L;
		requestApproved = false;
		requestedAmount = 0;
	}

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
