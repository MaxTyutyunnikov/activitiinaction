package org.bpmnwithactiviti.chapter9;

import java.io.Serializable;


public class BookOrder implements Serializable {

  private static final long serialVersionUID = 1L;
  
	private String isbn;
	private int amount;
	private boolean approved;
	
	public String getIsbn() {
  	return isbn;
  }
	public void setIsbn(String isbn) {
  	this.isbn = isbn;
  }
	public int getAmount() {
  	return amount;
  }
	public void setAmount(int amount) {
  	this.amount = amount;
  }
	public boolean isApproved() {
  	return approved;
  }
	public void setApproved(boolean approved) {
  	this.approved = approved;
  }
}
