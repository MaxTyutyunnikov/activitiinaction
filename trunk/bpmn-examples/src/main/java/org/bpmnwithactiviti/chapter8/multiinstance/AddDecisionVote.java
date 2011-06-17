package org.bpmnwithactiviti.chapter8.multiinstance;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AddDecisionVote implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String assignee = (String) execution.getVariableLocal("assignee");
		Boolean voteOutcome = (Boolean) execution.getVariable("vote");
		Vote vote = new Vote();
		vote.setName(assignee);
		vote.setApproved(voteOutcome);
		DecisionVoting voting = (DecisionVoting) execution.getVariable("voteOutcome");
		voting.addVote(vote);
		execution.setVariable("voteOutcome", voting);
	}

}
