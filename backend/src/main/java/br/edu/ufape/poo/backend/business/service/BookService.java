package br.edu.ufape.poo.backend.business.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.basic.Book;
import br.edu.ufape.poo.backend.data.BookRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookService
{
	@Autowired
	private BookRepository bookRepository;
	
	public Book findByApiId(String apiId) throws Exception
	{
		Optional<Book> bookOptional = bookRepository.findByApiId(apiId);
		if (!bookOptional.isPresent())
		{
			return null;
		}
		Book book = bookOptional.get();
		return book;
	}
	
	public Double findScoreByApiId(String apiId) throws Exception
	{
		Optional<Book> bookOptional = bookRepository.findByApiId(apiId);
		if (!bookOptional.isPresent())
		{
			return null;
		}
		Book book = bookOptional.get();
		Double score = (((double) book.getScoreTotal())/((double) book.getReviewCount()));
		return score;
	}
}