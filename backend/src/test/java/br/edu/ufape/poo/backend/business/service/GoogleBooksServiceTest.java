package br.edu.ufape.poo.backend.business.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class GoogleBooksServiceTest {
	@Autowired
	private GoogleBooksServiceInterface googleBooksService;

	@Test
	public void advancedSearchTerm() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults("flowers", null, null, null, null, null, 0, 1, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchTitle() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, "flowers", null, null, null, null, 0, 1, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchAuthor() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, null, "shakespeare", null, null, null, 0, 1, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchSubject() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, null, null, "fiction", null, null, 0, 1, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchPublisher() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, null, null, null, "penguin", null, 0, 1, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchIsbn() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, null, null, null, null, "9786555524697", 0, 1, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchMaxResults() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, null, null, null, "penguin", null, 0, 3, "incomplete");
		assertFalse(results.isEmpty());
	}

	@Test
	public void advancedSearchStartIndex() {
		List<Map<String, Object>> results = googleBooksService.advancedSearchResults(null, null, null, null, "penguin", null, 3, 1,"incomplete");
		assertFalse(results.isEmpty());
	}

}
