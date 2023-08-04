package br.edu.ufape.poo.backend.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.business.service.AccountService;
import br.edu.ufape.poo.backend.business.service.BookService;
import br.edu.ufape.poo.backend.business.service.BookshelfService;
import br.edu.ufape.poo.backend.business.service.GoogleBooksService;
import br.edu.ufape.poo.backend.exceptions.AccessDeniedException;

@Service
public class Facade
{
	@Autowired
	private AccountService accountService;
	@Autowired
	private BookService bookService;
	@Autowired
	private BookshelfService bookshelfService;
	@Autowired
	private GoogleBooksService googleBooksService;
	
	// Account
	
	public Account accountSignUp(Account account) throws Exception
	{
		Account newAccount = accountService.signUp(account);
		return newAccount;
	}
	
	public Account accountLogIn(String email, String password) throws Exception
	{
		Account account = accountService.logIn(email, password);
		return account;
	}
	
	public Account accountUpdate(Account account, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != account.getId())
		{
			throw new AccessDeniedException();
		}
		Account newAccount = accountService.update(account);
		return newAccount;
	}
	
	public Account accountDeleteById(Long id, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != id)
		{
			throw new AccessDeniedException();
		}
		Account oldAccount = accountService.deleteById(id);
		// Delete other stuff
		return oldAccount;
	}
	
	public Map<String, Object> accountFindOwnById(Long id, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != id)
		{
			throw new AccessDeniedException();
		}
		Account account = accountService.findById(id);
		int bookshelfCount = bookshelfService.countByOwnerId(account.getId());
		Map<String, Object> accountProfile = new HashMap<>();
		accountProfile.put("id", account.getId());
		accountProfile.put("name", account.getName());
		accountProfile.put("email", account.getEmail());
		accountProfile.put("password", account.getPassword());
		accountProfile.put("biography", account.getBiography());
		accountProfile.put("picture", account.getPicture());
		accountProfile.put("privacy", account.isPrivacy());
		accountProfile.put("bookshelfCount", bookshelfCount);
		return accountProfile;
	}
	
	public Map<String, Object> accountFindById(Long id) throws Exception
	{
		Account account = accountService.findById(id);
		Map<String, Object> accountProfile = new HashMap<>();
		accountProfile.put("id", account.getId());
		accountProfile.put("name", account.getName());
		accountProfile.put("picture", account.getPicture());
		accountProfile.put("privacy", account.isPrivacy());
		if (account.isPrivacy())
		{
			int bookshelfCount = bookshelfService.countByOwnerId(id);
			accountProfile.put("email", account.getEmail());
			accountProfile.put("biography", account.getBiography());
			accountProfile.put("bookshelfCount", bookshelfCount);
		}
		return accountProfile;
	}
	
	// Bookshelf
	
	public Bookshelf bookshelfCreate(Bookshelf bookshelf) throws Exception
	{
		accountService.authenticate(bookshelf.getOwner().getEmail(), bookshelf.getOwner().getPassword());
		Bookshelf newBookshelf = bookshelfService.create(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf bookshelfUpdate(Bookshelf bookshelf, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.update(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf bookshelfAddBookApiIdById(Long id, String apiId, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.addBookApiIdById(id, apiId);
		return newBookshelf;
	}
	
	public Bookshelf bookshelfRemoveBookApiIdById(Long id, String apiId, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.removeBookApiIdById(id, apiId);
		return newBookshelf;
	}
	
	public Bookshelf bookshelfDeleteById(Long id, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		Bookshelf oldBookshelf = bookshelfService.deleteById(id);
		return oldBookshelf;
	}
	
	public Map<String, Object> bookshelfFindOwnById(Long id, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		Map<String, Object> bookshelfCard = bookshelfFindByIdUtility(id, true);
		return bookshelfCard;
	}
	
	public Map<String, Object> bookshelfFindById(Long id) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (!bookshelf.getOwner().isPrivacy())
		{
			throw new AccessDeniedException();
		}
		Map<String, Object> bookshelfCard = bookshelfFindByIdUtility(id, false);
		return bookshelfCard;
	}
	
	public List<Map<String, Object>> bookshelfFindOwnByOwnerIdPaginate(Long ownerId, int offset, int limit, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != ownerId)
		{
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfCards = bookshelfFindByOwnerIdPaginateUtility(ownerId, offset, limit, true);
		return bookshelfCards;
	}
	
	public List<Map<String, Object>> bookshelfFindByOwnerIdPaginate(Long ownerId, int offset, int limit) throws Exception
	{
		Account account = accountService.findById(ownerId);
		if (!account.isPrivacy())
		{
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfCards = bookshelfFindByOwnerIdPaginateUtility(ownerId, offset, limit, false);
		return bookshelfCards;
	}
	
	public List<Map<String, Object>> bookshelfFindOwnBooksByIdPaginate(Long id, int offset, int limit, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> books = bookshelfFindBooksByIdPaginateUtility(id, offset, limit);
		return books;
	}
	
	public List<Map<String, Object>> bookshelfFindBooksByIdPaginate(Long id, int offset, int limit) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (!bookshelf.isPrivacy() || !bookshelf.getOwner().isPrivacy())
		{
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> books = bookshelfFindBooksByIdPaginateUtility(id, offset, limit);
		return books;
	}
	
	public List<Map<String, Object>> bookshelfFindOwnSelectByOwnerId(Long ownerId, String apiId, String email, String password) throws Exception
	{
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(ownerId);
		if (requestingAccount.getId() != bookshelf.getOwner().getId())
		{
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfSelects = bookshelfFindSelectByOwnerIdUtility(ownerId, apiId);
		return bookshelfSelects;
	}
	
	public List<Map<String, Object>> bookshelfFindSelectByOwnerId(Long ownerId, String apiId) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(ownerId);
		if (!bookshelf.getOwner().isPrivacy())
		{
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfSelects = bookshelfFindSelectByOwnerIdUtility(ownerId, apiId);
		return bookshelfSelects;
	}
	
	// Utility
	
	public Map<String, Object> bookshelfFindByIdUtility(Long id, boolean complete) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(id);
		Map<String, Object> bookshelfCard = new HashMap<>();
		bookshelfCard.put("id", bookshelf.getId());
		bookshelfCard.put("name", bookshelf.getName());
		bookshelfCard.put("privacy", bookshelf.isPrivacy());
		bookshelfCard.put("owner", bookshelf.getOwner());
		if (bookshelf.isPrivacy() || complete)
		{
			List<String> covers = new ArrayList<String>();
			Iterator<String> bookApiIdsIterator = bookshelf.getBookApiIds().listIterator();
			int index = 0;
			while (bookApiIdsIterator.hasNext() && index < 3)
			{
				String bookApiId = bookApiIdsIterator.next();
				index++;
				String cover = googleBooksService.findCoverByApiId(bookApiId);
				covers.add(cover);
			}
			bookshelfCard.put("description", bookshelf.getDescription());
			bookshelfCard.put("bookApiIds", bookshelf.getBookApiIds());
			bookshelfCard.put("creationDate", bookshelf.getCreationDate());
			bookshelfCard.put("covers", covers);
		}
		return bookshelfCard;
	}
	
	
	private List<Map<String, Object>> bookshelfFindByOwnerIdPaginateUtility(Long ownerId, int offset, int limit, boolean complete) throws Exception
	{
		int bookshelfCount = bookshelfService.countByOwnerId(ownerId);
		if (offset < 0)
		{
			offset = 0;
		}
		if (limit < 0)
		{
			limit = 0;
		}
		if (offset > bookshelfCount)
		{
			offset = bookshelfCount;
		}
		if (limit > bookshelfCount-offset)
		{
			limit = bookshelfCount-offset;
		}
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerIdPaginate(ownerId, offset, limit);
		List<Map<String, Object>> bookshelfCards = new ArrayList<Map<String, Object>>();
		Iterator<Bookshelf> bookshelvesIterator = bookshelves.listIterator();
		int index = 0;
		while (bookshelvesIterator.hasNext())
		{
			Bookshelf bookshelf = bookshelvesIterator.next();
			Map<String, Object> bookshelfCard = new HashMap<>();
			bookshelfCard.put("id", bookshelf.getId());
			bookshelfCard.put("name", bookshelf.getName());
			bookshelfCard.put("privacy", bookshelf.isPrivacy());
			bookshelfCard.put("owner", bookshelf.getOwner());
			if (bookshelf.isPrivacy() || complete)
			{
				List<String> covers = new ArrayList<String>();
				Iterator<String> bookApiIdsIterator = bookshelf.getBookApiIds().listIterator();
				index = 0;
				while (bookApiIdsIterator.hasNext() && index < 3)
				{
					String bookApiId = bookApiIdsIterator.next();
					index++;
					String cover = googleBooksService.findCoverByApiId(bookApiId);
					covers.add(cover);
				}
				bookshelfCard.put("description", bookshelf.getDescription());
				bookshelfCard.put("bookApiIds", bookshelf.getBookApiIds());
				bookshelfCard.put("creationDate", bookshelf.getCreationDate());
				bookshelfCard.put("covers", covers);
			}
			bookshelfCards.add(bookshelfCard);
		}
		return bookshelfCards;
	}
	
	private List<Map<String, Object>> bookshelfFindBooksByIdPaginateUtility(Long id, int offset, int limit) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (offset < 0)
		{
			offset = 0;
		}
		if (limit < 0)
		{
			limit = 0;
		}
		if (offset > bookshelf.getBookApiIds().size())
		{
			offset = bookshelf.getBookApiIds().size();
		}
		if (limit > bookshelf.getBookApiIds().size()-offset)
		{
			limit = bookshelf.getBookApiIds().size()-offset;
		}
		List<Map<String, Object>> books = new ArrayList<Map<String, Object>>();
		List<String> bookApiIds = bookshelf.getBookApiIds().subList(offset, offset+limit);
		Iterator<String> bookApiIdsIterator = bookApiIds.listIterator();
		while (bookApiIdsIterator.hasNext())
		{
			String bookApiId = bookApiIdsIterator.next();
			Map<String, Object> book = googleBooksService.findByApiId(bookApiId);
			book.put("apiId", bookApiId);
			Double score = bookService.findScoreByApiId(bookApiId);
			if (score != null)
			{
				score = ((double) Math.round(score*10d))/10d;
				book.put("score", score);
			}
			else
			{
				book.put("score", null);
			}
			books.add(book);
		}
		return books;
	}
	
	private List<Map<String, Object>> bookshelfFindSelectByOwnerIdUtility(Long id, String apiId) throws Exception
	{
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerId(id);
		List<Map<String, Object>> bookshelfSelects = new ArrayList<Map<String, Object>>();
		Iterator<Bookshelf> bookshelvesIterator = bookshelves.listIterator();
		while (bookshelvesIterator.hasNext())
		{
			Bookshelf bookshelf = bookshelvesIterator.next();
			Map<String, Object> bookshelfSelect = new HashMap<>();
			bookshelfSelect.put("id", bookshelf.getId());
			bookshelfSelect.put("name", bookshelf.getName());
			if (bookshelf.getBookApiIds().contains(apiId))
			{
				bookshelfSelect.put("contais", true);
			}
			else
			{
				bookshelfSelect.put("contais", false);
			}
			bookshelfSelects.add(bookshelfSelect);
		}
		return bookshelfSelects;
	}
}