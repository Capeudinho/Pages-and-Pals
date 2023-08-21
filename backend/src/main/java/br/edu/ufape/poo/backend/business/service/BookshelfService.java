package br.edu.ufape.poo.backend.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.data.BookshelfRepository;
import br.edu.ufape.poo.backend.exceptions.DuplicateApiIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectApiIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidDescriptionException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameException;
import br.edu.ufape.poo.backend.util.OffsetPageRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookshelfService implements BookshelfServiceInterface
{
	@Autowired
	private BookshelfRepository bookshelfRepository;
	
	public Bookshelf create(Bookshelf bookshelf) throws Exception
	{
		if (bookshelf.getName() == null || bookshelf.getName().isBlank())
		{
			throw new InvalidNameException();
		}
		if (bookshelf.getDescription() == null)
		{
			throw new InvalidDescriptionException();
		}
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf.setCreationDate(LocalDate.now());
		Bookshelf newBookshelf = bookshelfRepository.save(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf update(Bookshelf bookshelf) throws Exception
	{
		Bookshelf oldBookshelf = bookshelfRepository.findById(bookshelf.getId()).orElse(null);
		if (oldBookshelf == null)
		{
			throw new IncorrectIdException();
		}
		if (bookshelf.getName() == null || bookshelf.getName().isBlank())
		{
			throw new InvalidNameException();
		}
		if (bookshelf.getDescription() == null)
		{
			throw new InvalidDescriptionException();
		}
		bookshelf.setBookApiIds(oldBookshelf.getBookApiIds());
		bookshelf.setCreationDate(oldBookshelf.getCreationDate());
		bookshelf.setOwner(oldBookshelf.getOwner());
		Bookshelf newBookshelf = bookshelfRepository.save(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf addBookApiIdById(Long id, String bookApiId) throws Exception
	{
		Bookshelf bookshelf = bookshelfRepository.findById(id).orElse(null);
		if (bookshelf == null)
		{
			throw new IncorrectIdException();
		}
		if (bookshelf.getBookApiIds().contains(bookApiId))
		{
			throw new DuplicateApiIdException();
		}
		List<String> newBookApiIds = bookshelf.getBookApiIds();
		newBookApiIds.add(bookApiId);
		Bookshelf newBookshelf = bookshelfRepository.save(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf removeBookApiIdById(Long id, String bookApiId) throws Exception
	{
		Bookshelf bookshelf = bookshelfRepository.findById(id).orElse(null);
		if (bookshelf == null)
		{
			throw new IncorrectIdException();
		}
		if (!bookshelf.getBookApiIds().contains(bookApiId))
		{
			throw new IncorrectApiIdException();
		}
		List<String> newBookApiIds = bookshelf.getBookApiIds();
		newBookApiIds.remove(bookApiId);
		Bookshelf newBookshelf = bookshelfRepository.save(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf deleteById(Long id) throws Exception
	{
		Bookshelf bookshelf = bookshelfRepository.findById(id).orElse(null);
		if (bookshelf == null)
		{
			throw new IncorrectIdException();
		}
		bookshelfRepository.delete(bookshelf);
		return bookshelf;
	}
	
	public List<Bookshelf> deleteByOwnerId(Long ownerId) throws Exception
	{
		List<Bookshelf> bookshelves = bookshelfRepository.deleteByOwnerId(ownerId);
		return bookshelves;
	}
	
	public Bookshelf findById(Long id) throws Exception
	{
		Bookshelf bookshelf = bookshelfRepository.findById(id).orElse(null);
		if (bookshelf == null)
		{
			throw new IncorrectIdException();
		}
		return bookshelf;
	}
	
	public List<Bookshelf> findByOwnerId(Long id)
	{
		List<Bookshelf> bookshelves = bookshelfRepository.findByOwnerIdOrderByCreationDateDesc(id);
		return bookshelves;
	}
	
	public List<Bookshelf> findByOwnerIdPaginate(Long id, int offset, int limit)
	{
		Pageable pageable = new OffsetPageRequest(offset, limit);
		List<Bookshelf> bookshelves = bookshelfRepository.findByOwnerIdOrderByCreationDateDesc(id, pageable);
		return bookshelves;
	}
	
	public List<Object> findAdvanced(String ownerName, String bookshelfName)
	{
		List<Object> bookshelves = bookshelfRepository.findByOwnerAndBookshelfName(ownerName, bookshelfName);
		return bookshelves;
	}
	
	public int countByOwnerId(Long id)
	{
		int bookshelfCount = bookshelfRepository.countByOwnerId(id);
		return bookshelfCount;
	}
}