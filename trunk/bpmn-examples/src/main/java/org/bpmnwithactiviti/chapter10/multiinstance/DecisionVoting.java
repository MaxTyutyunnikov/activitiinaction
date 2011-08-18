package org.bpmnwithactiviti.chapter10.multiinstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DecisionVoting implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Vote> voteList = new ArrayList<Vote>();
	private boolean decisionVotingOutcome;
	
	public void addVote(Vote vote) {
		voteList.add(vote);
	}
	
	public List<Vote> getVotes() {
		return voteList;
	}

	public boolean isDecisionVotingOutcome() {
		return decisionVotingOutcome;
	}

	public void setDecisionVotingOutcome(boolean decisionVotingOutcome) {
		this.decisionVotingOutcome = decisionVotingOutcome;
	}
}
