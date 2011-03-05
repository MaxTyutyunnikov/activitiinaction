package org.bpmnwithactiviti.chapter5.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.pvm.delegate.TaskListener;

public class GossipAboutUserTask {
	
	public void gossipTask(DelegateTask task, String eventName) {
		if(TaskListener.EVENTNAME_CREATE.equals(eventName)) {
			task.setAssignee("John");
			System.out.println("Drink user task is created and assigned to John");
		} else if(TaskListener.EVENTNAME_ASSIGNMENT.equals(eventName)) {
			System.out.println("Drink user task is assigned to " + task.getAssignee());
		}
		EventUtil.addEvent(task, eventName);
	}

}
