package org.bpmnwithactiviti.chapter5.event;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;

public class EventUtil {
	
	@SuppressWarnings("unchecked")
	public static void addEvent(ExecutionListenerExecution execution, String source) {
		List<String> eventList = (List<String>) execution.getVariable("eventList");
		if(eventList == null) {
			eventList = new ArrayList<String>();
		}
		eventList.add(source + ":" + execution.getEventName());
		execution.setVariable("eventList", eventList);
	}
	
	@SuppressWarnings("unchecked")
	public static void addEvent(DelegateTask task, String event) {
		List<String> eventList = (List<String>) task.getExecution().getVariable("eventList");
		if(eventList == null) {
			eventList = new ArrayList<String>();
		}
		eventList.add("usertask:" + event);
		task.getExecution().setVariable("eventList", eventList);
	}

}
