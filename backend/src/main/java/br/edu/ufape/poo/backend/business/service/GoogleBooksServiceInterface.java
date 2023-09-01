package br.edu.ufape.poo.backend.business.service;

import java.util.List;
import java.util.Map;

public interface GoogleBooksServiceInterface 
{
	public Map<String, Object> findByApiId(String apiId, String extractInfo) throws Exception;
	public String findCoverByApiId(String id);
	public List<Object> advancedSearchResults(String term, String title, String author, String subject,
			String publisher, String isbn, Integer maxResults, Integer startIndex, String extractInfo);
}
