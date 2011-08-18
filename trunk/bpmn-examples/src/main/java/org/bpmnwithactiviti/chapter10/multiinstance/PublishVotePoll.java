package org.bpmnwithactiviti.chapter10.multiinstance;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class PublishVotePoll implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		DecisionVoting voting = (DecisionVoting) execution.getVariable("voteOutcome");
		int approved = 0;
		for(Vote vote : voting.getVotes()) {
			System.out.println(vote.getName() + " voted " + vote.isApproved());
			if(vote.isApproved() == true) {
				approved++;
			}
		}
		if(approved > (voting.getVotes().size() / 2.0)) {
			voting.setDecisionVotingOutcome(true);
			System.out.println("Voting outcome: approved");
		} else {
			voting.setDecisionVotingOutcome(false);
			System.out.println("Voting outcome: disapproved");
		}
		execution.setVariable("voteOutcome", voting);
	}

}
