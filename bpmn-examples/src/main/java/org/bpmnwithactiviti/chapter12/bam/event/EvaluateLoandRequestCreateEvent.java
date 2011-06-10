package org.bpmnwithactiviti.chapter12.bam.event;

public class EvaluateLoandRequestCreateEvent {
	
	// For correlating the events.
	private final String processInstanceId;
	private final int completeTime;
	
	public EvaluateLoandRequestCreateEvent(String processInstanceId, int completeTime) {
		this.processInstanceId = processInstanceId;
		this.completeTime = completeTime;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public int getCompleteTime() {
		return completeTime;
	}
}
