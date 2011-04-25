package org.bpmnwithactiviti.chapter9.model;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 7783916729430463348L;
	private int age;
	private String name;
	private boolean isAdult;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAdult() {
		return isAdult;
	}
	public void setAdult(boolean isAdult) {
		this.isAdult = isAdult;
	}

}
