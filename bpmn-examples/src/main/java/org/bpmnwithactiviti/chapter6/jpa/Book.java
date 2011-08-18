package org.bpmnwithactiviti.chapter6.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Book {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String title;
	
	private String subTitle;
	
	private String isbn;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private List<String> authors;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
  public List<String> getAuthors() {
    if(authors == null) {
      authors = new ArrayList<String>();
    }
    return authors;
  }
  public void setAuthors(List<String> authors) {
    this.authors = authors;
  }
}
