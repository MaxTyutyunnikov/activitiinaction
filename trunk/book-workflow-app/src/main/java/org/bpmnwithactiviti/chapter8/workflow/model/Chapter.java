package org.bpmnwithactiviti.chapter8.workflow.model;

import java.io.Serializable;
import java.util.Date;

public class Chapter implements Serializable {
	
  private static final long serialVersionUID = 1L;
  int chapterNumber;
	String title;
	String author;
	String filename;
	int version;
	Date lastDraftUploadDate;
	Date lastReviewDate;
	boolean approvedByReviewer;
	
	public int getChapterNumber() {
  	return chapterNumber;
  }
	public Chapter setChapterNumber(int chapterNumber) {
  	this.chapterNumber = chapterNumber;
  	return this;
  }
	public String getTitle() {
  	return title;
  }
	public Chapter setTitle(String title) {
  	this.title = title;
  	return this;
  }
	public String getAuthor() {
  	return author;
  }
	public Chapter setAuthor(String author) {
  	this.author = author;
  	return this;
  }
	public String getFilename() {
  	return filename;
  }
	public Chapter setFilename(String filename) {
  	this.filename = filename;
  	return this;
  }
	public int getVersion() {
  	return version;
  }
	public Chapter setVersion(int version) {
  	this.version = version;
  	return this;
  }
	public Date getLastDraftUploadDate() {
  	return lastDraftUploadDate;
  }
	public Chapter setLastDraftUploadDate(Date lastDraftUploadDate) {
  	this.lastDraftUploadDate = lastDraftUploadDate;
  	return this;
  }
	public Date getLastReviewDate() {
  	return lastReviewDate;
  }
	public Chapter setLastReviewDate(Date lastReviewDate) {
  	this.lastReviewDate = lastReviewDate;
  	return this;
  }
	public boolean isApprovedByReviewer() {
  	return approvedByReviewer;
  }
	public Chapter setApprovedByReviewer(boolean approvedByReviewer) {
  	this.approvedByReviewer = approvedByReviewer;
  	return this;
  }
}
