package org.bpmnwithactiviti.chapter8.workflow.model;

import java.io.Serializable;
import java.util.Date;

public class Chapter implements Serializable {
	
  private static final long serialVersionUID = 1L;
  Integer chapterNumber;
  String bookTitle;
	String chapterTitle;
	String author;
	String filename;
	int version;
	Date lastDraftUploadDate;
	Date lastReviewDate;
	boolean approvedByReviewer;
	
	public Integer getChapterNumber() {
  	return chapterNumber;
  }
	public Chapter setChapterNumber(Integer chapterNumber) {
  	this.chapterNumber = chapterNumber;
  	return this;
	}
	public String getBookTitle() {
  	return bookTitle;
  }
	public Chapter setBookTitle(String bookTitle) {
  	this.bookTitle = bookTitle;
  	return this;
  }
	public String getChapterTitle() {
  	return chapterTitle;
  }
	public Chapter setChapterTitle(String chapterTitle) {
  	this.chapterTitle = chapterTitle;
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
