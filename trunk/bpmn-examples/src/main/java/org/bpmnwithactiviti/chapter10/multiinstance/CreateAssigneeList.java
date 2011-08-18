package org.bpmnwithactiviti.chapter10.multiinstance;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateAssigneeList implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String participants = (String) execution.getVariable("participants");
		String[] participantsArray = participants.split(",");
		List<String> assigneeList = new ArrayList<String>();
		for (String assignee : participantsArray) {
			assigneeList.add(assignee);
		}
		execution.setVariable("assigneeList", assigneeList);
		execution.setVariable("voteOutcome", new DecisionVoting());
	}

}
