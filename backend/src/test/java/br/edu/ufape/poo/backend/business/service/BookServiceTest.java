package br.edu.ufape.poo.backend.business.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ufape.poo.backend.business.entity.Book;
import br.edu.ufape.poo.backend.data.BookRepository;
import br.edu.ufape.poo.backend.exceptions.IncorrectBookIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidReviewCountException;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class BookServiceTest {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookRepository bookRepository;

	@Test
	void testCreate() {
		Book b;
		b = new Book();
		b.setReviewCount(5);
		b = bookService.create(b);
		
		assertNotEquals(5, b.getReviewCount());
	}
	
	@Test
	void testUpdate() {
		Book b = new Book();
		Book bUpdate = new Book();
		int reviewsCount = 5;
	
		b.setReviewCount(reviewsCount);
		bookRepository.save(b);
		b.setReviewCount(7);
		
		try {
		bookService.update(b);
		bUpdate = bookService.findByApiId(b.getApiId());
		} catch (Exception e){
			fail();	
		}
		
		assertNotEquals(bUpdate.getReviewCount(),reviewsCount);
	}
	
	@Test
	void testUpdateReviewCountLessThanZero() {
		Book b = new Book();
	
		b.setReviewCount(5);
		bookRepository.save(b);
		b.setReviewCount(-6);
		
		assertThrows(InvalidReviewCountException.class, () -> {
			bookService.update(b);
        });
	}
	
	@Test
	void testDeleteByApiId() {
		Book b = new Book();
		Book oldB = new Book();
		String apiId = b.getApiId();
		
		bookRepository.save(b);
	
	try {
		bookService.deleteByApiId(apiId);
		oldB = bookService.findByApiId(apiId);
		} catch (Exception e){
			fail();	
		}
	
		assertEquals(null, oldB);
		
	}
	
	@Test
	void testDeleteByApiIdTwice() {
		Book b = new Book();
		String apiId = b.getApiId();
		
		bookRepository.save(b);
	
	try {
		bookService.deleteByApiId(apiId);
		} catch (Exception e){
			fail();	
		}
	
	assertThrows(IncorrectBookIdException.class, () -> {
		bookService.deleteByApiId(apiId);;
    });
		
	}
	
}
