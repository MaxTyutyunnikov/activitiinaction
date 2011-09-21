package org.bpmnwithactiviti.cdi.bean;

import java.io.Serializable;

import javax.inject.Named;

import org.activiti.cdi.annotation.BusinessProcessScoped;

@BusinessProcessScoped
@Named
public class BookOrder implements Serializable {

  private static final long serialVersionUID = 1L;
  
	private String isbn;
	private boolean approved;
	
	public String getIsbn() {
  	return isbn;
  }
	public void setIsbn(String isbn) {
  	this.isbn = isbn;
  }
	public boolean isApproved() {
  	return approved;
  }
	public void setApproved(boolean approved) {
  	this.approved = approved;
  }
}
