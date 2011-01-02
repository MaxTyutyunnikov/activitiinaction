package org.bpmnwithactiviti.chapter5.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

public class BookService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Book createBook(String title, String isbn) {
		Book book = new Book();
		book.setTitle(title);
		book.setIsbn(isbn);
		entityManager.persist(book);
		return book;
	}

}
