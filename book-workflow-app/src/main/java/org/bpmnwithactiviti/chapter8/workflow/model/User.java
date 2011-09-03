package org.bpmnwithactiviti.chapter8.workflow.model;

import java.io.Serializable;

public class User implements Serializable {
	
  private static final long serialVersionUID = 1L;
	public static final String ROLE_USER = "role_user";
	public static final String ROLE_ADMIN = "role_admin";
	public static final String ROLE_REVIEWER = "role_reviewer";
	
	String username;
	String password;
	String role;
	int numberOfReviews;
	
	public String getUsername() {
  	return username;
  }
	
	public User setUsername(String username) {
  	this.username = username;
  	return this;
  }
	
	public String getPassword() {
  	return password;
  }
	
	public User setPassword(String password) {
  	this.password = password;
  	return this;
  }
	
	public String getRole() {
  	return role;
  }
	
	public User setRole(String role) {
  	this.role = role;
  	return this;
	}

	public int getNumberOfReviews() {
  	return numberOfReviews;
  }

	public User setNumberOfReviews(int numberOfReviews) {
  	this.numberOfReviews = numberOfReviews;
  	return this;
  }
}
