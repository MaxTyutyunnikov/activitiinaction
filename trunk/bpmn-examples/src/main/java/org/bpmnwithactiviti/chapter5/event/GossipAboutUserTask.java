package org.bpmnwithactiviti.chapter5.event;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.impl.pvm.delegate.TaskListener;

public class GossipAboutUserTask implements TaskListener {
	
	private Expression event;

	@Override
	public void notify(DelegateTask task) {
		String eventName = event.getValue(task.getExecution()).toString();
		if(TaskListener.EVENTNAME_CREATE.equals(eventName)) {
			task.setAssignee("John");
			System.out.println("Drink user task is created and assigned to John");
		} else if(TaskListener.EVENTNAME_ASSIGNMENT.equals(eventName)) {
			System.out.println("Drink user task is assigned to " + task.getAssignee());
		}
		EventUtil.addEvent(task, eventName);
	}

}
