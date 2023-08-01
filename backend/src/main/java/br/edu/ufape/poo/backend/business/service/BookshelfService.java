package br.edu.ufape.poo.backend.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.business.basic.Bookshelf;
import br.edu.ufape.poo.backend.data.BookshelfRepository;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameBookshelfException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookshelfService
{
	@Autowired
	private BookshelfRepository bookshelfRepository;
	
	public Bookshelf create(Bookshelf bookshelf) throws Exception
	{
		if (bookshelf.getName().isBlank())
		{
			throw new InvalidNameBookshelfException(bookshelf);
		}
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf.setCreationDate(LocalDate.now());
		Bookshelf newBookshelf = bookshelfRepository.save(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf update(Bookshelf bookshelf) throws Exception
	{
		Optional<Bookshelf> bookshelfOptional = bookshelfRepository.findById(bookshelf.getId());
		if (!bookshelfOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		if (bookshelf.getName().isBlank())
		{
			throw new InvalidNameBookshelfException(bookshelf);
		}
		Bookshelf newAccount = bookshelfRepository.save(bookshelf);
		return newAccount;
	}
	
	public List<Bookshelf> findByOwnerPaginate(Account account, int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		List<Bookshelf> bookshelves = bookshelfRepository.findByOwner(account, pageable);
		return bookshelves;
	}
	
	public Bookshelf findById(Long id) throws Exception
	{
		Optional<Bookshelf> bookshelfOptional = bookshelfRepository.findById(id);
		if (!bookshelfOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		Bookshelf bookshelf = bookshelfOptional.get();
		return bookshelf;
	}
	
	public Bookshelf deleteById(Long id) throws Exception
	{
		Optional<Bookshelf> bookshelfOptional = bookshelfRepository.findById(id);
		if (!bookshelfOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		bookshelfRepository.delete(bookshelfOptional.get());
		return bookshelfOptional.get();
	}
}