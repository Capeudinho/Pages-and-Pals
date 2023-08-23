package br.edu.ufape.poo.backend.business.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class GoogleBooksServiceTest 
{
	@Autowired
	private GoogleBooksServiceInterface googleBooksService;
	
	@Test
	public void advancedSearchTerm() {
        List<Object> results = googleBooksService.advancedSearchResults("flowers", null, null, null, null, null, null, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchTitle() {
        List<Object> results = googleBooksService.advancedSearchResults(null, "flowers", null, null, null, null, null, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchAuthor() {
        List<Object> results = googleBooksService.advancedSearchResults(null, null, "shakespeare", null, null, null, null, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchSubject() {
        List<Object> results = googleBooksService.advancedSearchResults(null, null, null, "fiction", null, null, null, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchPublisher() {
        List<Object> results = googleBooksService.advancedSearchResults(null, null, null, null, "penguin", null, null, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchIsbn() {
        List<Object> results = googleBooksService.advancedSearchResults(null, null, null, null, null, "9786555524697", null, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchMaxResults() {
        List<Object> results = googleBooksService.advancedSearchResults(null, null, null, null, "penguin", null, 3, null, "incomplete");
        assertFalse(results.isEmpty());
    }
	
	@Test
	public void advancedSearchStartIndex() {
        List<Object> results = googleBooksService.advancedSearchResults(null, null, null, null, "penguin", null, 3, 0, "incomplete");
        assertFalse(results.isEmpty());
    }
	

}

	