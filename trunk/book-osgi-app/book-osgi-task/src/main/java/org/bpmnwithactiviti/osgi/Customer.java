package org.bpmnwithactiviti.osgi;

import java.io.Serializable;

public class Customer implements Serializable {
	
  private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
  	return name;
  }

	public void setName(String name) {
  	this.name = name;
  }
}
