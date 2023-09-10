package br.edu.ufape.poo.backend.business.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.entity.Book;
import br.edu.ufape.poo.backend.data.BookRepository;
import br.edu.ufape.poo.backend.exceptions.IncorrectBookIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidReviewCountException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookService implements BookServiceInterface {
	@Autowired
	private BookRepository bookRepository;
	
	public Book create(Book book) {
		book.setReviewCount(0);
		book.setScoreTotal(0.0);
		Book newBook = bookRepository.save(book);
		return newBook;
	}

	public Book update(Book book) throws Exception {
		if (book.getReviewCount() < 0) {
			throw new InvalidReviewCountException();
		}
		Book newBook = bookRepository.save(book);
		return newBook;
	}

	public Book deleteByApiId(String apiId) throws Exception {
		Book book = bookRepository.findByApiId(apiId).orElse(null);
		if (book == null) {
			throw new IncorrectBookIdException();
		}
		bookRepository.delete(book);
		return book;
	}

	public Book findByApiId(String apiId) throws Exception {
		Optional<Book> bookOptional = bookRepository.findByApiId(apiId);
		if (!bookOptional.isPresent()) {
			return null;
		}
		Book book = bookOptional.get();
		return book;
	}

	public Double findScoreByApiId(String apiId) throws Exception {
		Optional<Book> bookOptional = bookRepository.findByApiId(apiId);
		if (!bookOptional.isPresent()) {
			return null;
		}
		Book book = bookOptional.get();
		Double score = ((book.getScoreTotal()) / ((double) book.getReviewCount()));
		return score;
	}
}