package org.bpmnwithactiviti.cdi.task;

import javax.inject.Inject;
import javax.inject.Named;

import org.bpmnwithactiviti.cdi.bean.BookFeed;
import org.bpmnwithactiviti.cdi.bean.BookOrder;

@Named
public class BookOrderTask {

	@Inject
	private BookOrder book;
	
	@Inject
	private BookFeed bookFeed;
	
	public void validate() {
		long numberIsbn = Long.valueOf(book.getIsbn());
		if(numberIsbn < 100000 || numberIsbn > 999999) {
			System.out.println(">>> Invalid ISBN: " + numberIsbn);
		}
	}
	
	public void approve() {
		if(book.isApproved()) {
			bookFeed.addApprovedIsbn(book.getIsbn());
		} else {
			bookFeed.addNotApprovedIsbn(book.getIsbn());
		}
	}
}
