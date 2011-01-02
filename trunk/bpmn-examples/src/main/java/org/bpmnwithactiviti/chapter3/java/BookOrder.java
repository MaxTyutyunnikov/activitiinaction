package org.bpmnwithactiviti.chapter3.java;

import java.io.Serializable;
import java.util.Date;


public class BookOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Date validate(Long isbn) {
		System.out.println("received isbn " + isbn);
		return new Date();
	}
}
