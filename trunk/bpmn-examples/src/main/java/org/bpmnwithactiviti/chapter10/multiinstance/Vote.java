package org.bpmnwithactiviti.chapter10.multiinstance;

import java.io.Serializable;

public class Vote implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private boolean approved;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}
