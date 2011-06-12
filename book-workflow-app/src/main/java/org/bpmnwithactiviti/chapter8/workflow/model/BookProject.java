package org.bpmnwithactiviti.chapter8.workflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookProject implements Serializable {
	
  private static final long serialVersionUID = 1L;
	String title;
	int nrOfChapters;
	List<String> authors = new ArrayList<String>();
	String reviewer;
	String description;
	Date toProductionDate;
	List<Chapter> chapterList = new ArrayList<Chapter>();
	
	public String getTitle() {
  	return title;
  }
	public BookProject setTitle(String title) {
  	this.title = title;
  	return this;
  }
	public int getNrOfChapters() {
  	return nrOfChapters;
  }
	public BookProject setNrOfChapters(int nrOfChapters) {
  	this.nrOfChapters = nrOfChapters;
  	return this;
  }
	public List<String> getAuthors() {
  	return authors;
  }
	public BookProject setAuthors(List<String> authors) {
  	this.authors = authors;
  	return this;
  }
	public BookProject addAuthor(String author) {
		this.authors.add(author);
		return this;
	}
	public String getReviewer() {
  	return reviewer;
  }
	public BookProject setReviewer(String reviewer) {
  	this.reviewer = reviewer;
  	return this;
  }
	public String getDescription() {
  	return description;
  }
	public BookProject setDescription(String description) {
  	this.description = description;
  	return this;
	}
	public Date getToProductionDate() {
  	return toProductionDate;
  }
	public BookProject setToProductionDate(Date toProductionDate) {
  	this.toProductionDate = toProductionDate;
  	return this;
  }
	public List<Chapter> getChapterList() {
  	return chapterList;
  }
	public BookProject setChapterList(List<Chapter> chapterList) {
  	this.chapterList = chapterList;
  	return this;
  }
	public BookProject addChapter(Chapter chapter) {
		this.chapterList.add(chapter);
		return this;
	}
	
	@Override
  public String toString() {
	  return "BookProject [title=" + title + ", nrOfChapters=" + nrOfChapters
	      + ", authors=" + authors + ", reviewer=" + reviewer + ", description="
	      + description + ", toProductionDate=" + toProductionDate
	      + ", chapterList=" + chapterList + "]";
  }
}
