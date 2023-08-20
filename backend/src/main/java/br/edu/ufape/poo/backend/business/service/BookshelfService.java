package br.edu.ufape.poo.backend.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.data.BookshelfRepository;
import br.edu.ufape.poo.backend.exceptions.DuplicateApiIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectApiIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameBookshelfException;
import br.edu.ufape.poo.backend.util.OffsetPageRequest;
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
		Set<String> bookApiIds = new HashSet<String>(bookshelf.getBookApiIds());
		if (bookApiIds.size() < bookshelf.getBookApiIds().size())
		{
			throw new DuplicateApiIdException();
		}
		Bookshelf newAccount = bookshelfRepository.save(bookshelf);
		return newAccount;
	}
	
	public Bookshelf addBookApiIdById(Long id, String bookApiId) throws Exception
	{
		Optional<Bookshelf> bookshelfOptional = bookshelfRepository.findById(id);
		if (!bookshelfOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		if (bookshelfOptional.get().getBookApiIds().contains(bookApiId))
		{
			throw new DuplicateApiIdException();
		}
		List<String> newBookApiIds = bookshelfOptional.get().getBookApiIds();
		newBookApiIds.add(bookApiId);
		Bookshelf newAccount = bookshelfRepository.save(bookshelfOptional.get());
		return newAccount;
	}
	
	public Bookshelf removeBookApiIdById(Long id, String bookApiId) throws Exception
	{
		Optional<Bookshelf> bookshelfOptional = bookshelfRepository.findById(id);
		if (!bookshelfOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		if (!bookshelfOptional.get().getBookApiIds().contains(bookApiId))
		{
			throw new IncorrectApiIdException();
		}
		List<String> newBookApiIds = bookshelfOptional.get().getBookApiIds();
		newBookApiIds.remove(bookApiId);
		Bookshelf newAccount = bookshelfRepository.save(bookshelfOptional.get());
		return newAccount;
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
	
	public List<Bookshelf> findByOwnerIdPaginate(Long id, int offset, int limit)
	{
		Pageable pageable = new OffsetPageRequest(offset, limit);
		List<Bookshelf> bookshelves = bookshelfRepository.findByOwnerIdOrderByIdDesc(id, pageable);
		return bookshelves;
	}
	
	public List<Bookshelf> findByOwnerId(Long id)
	{
		List<Bookshelf> bookshelves = bookshelfRepository.findByOwnerIdOrderByIdDesc(id);
		return bookshelves;
	}
	
	public List<Object> findAdvanced(String ownerName, String bookshelfName)
	{
		List<Object> bookshelves = bookshelfRepository.findByOwnerAndBookshelfName(ownerName, bookshelfName);
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
	
	public int countByOwnerId(Long id)
	{
		int bookshelfCount = bookshelfRepository.countByOwnerId(id);
		return bookshelfCount;
	}
}