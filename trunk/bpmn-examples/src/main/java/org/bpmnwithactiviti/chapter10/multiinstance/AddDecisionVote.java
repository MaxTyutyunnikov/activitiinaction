package org.bpmnwithactiviti.chapter10.multiinstance;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AddDecisionVote implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String assignee = (String) execution.getVariableLocal("assignee");
		String voteOutcome = (String) execution.getVariable("vote");
		Vote vote = new Vote();
		vote.setName(assignee);
		vote.setApproved(Boolean.valueOf(voteOutcome));
		DecisionVoting voting = (DecisionVoting) execution.getVariable("voteOutcome");
		voting.addVote(vote);
		execution.setVariable("voteOutcome", voting);
	}

}
