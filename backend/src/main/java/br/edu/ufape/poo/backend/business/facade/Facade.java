package br.edu.ufape.poo.backend.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.business.basic.Bookshelf;
import br.edu.ufape.poo.backend.business.service.AccountService;
import br.edu.ufape.poo.backend.business.service.BookService;
import br.edu.ufape.poo.backend.business.service.BookshelfService;
import br.edu.ufape.poo.backend.business.service.GoogleBooksService;

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
	
	public Account accountUpdate(Account account) throws Exception
	{
		Account newAccount = accountService.update(account);
		return newAccount;
	}
	
	public Account accountFindById(Long id) throws Exception
	{
		Account account = accountService.findById(id);
		return account;
	}
	
	public Account accountDeleteById(Long id) throws Exception
	{
		Account account = accountService.deleteById(id);
		return account;
	}
	
	// Bookshelf
	
	public Bookshelf bookshelfCreate(Bookshelf bookshelf) throws Exception
	{
		Bookshelf newBookshelf = bookshelfService.create(bookshelf);
		return newBookshelf;
	}
	
	public Bookshelf bookshelfUpdate(Bookshelf bookshelf) throws Exception
	{
		Bookshelf newBookshelf = bookshelfService.update(bookshelf);
		return newBookshelf;
	}
	
	public List<Map<String, Object>> bookshelfFindCardsByOwnerId(Long id, int page, int size) throws Exception
	{
		Account account = accountService.findById(id);
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerPaginate(account, page, size);
		List<Map<String, Object>> bookshelfCards = new ArrayList<Map<String, Object>>();
		Iterator<Bookshelf> bookshelvesIterator = bookshelves.listIterator();
		int index = 0;
		while (bookshelvesIterator.hasNext())
		{
			Bookshelf bookshelf = bookshelvesIterator.next();
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
			Map<String, Object> cardInformation = new HashMap<>();
			cardInformation.put("bookshelf", bookshelf);
			cardInformation.put("covers", covers);
			bookshelfCards.add(cardInformation);
		}
		return bookshelfCards;
	}
	
	public Bookshelf bookshelfFindById(Long id) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(id);
		return bookshelf;
	}
	
	public List<Map<String, Object>> bookshelfFindBookCardsById (Long id, int page, int size) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.findById(id);
		List<Map<String, Object>> bookCards = new ArrayList<Map<String, Object>>();
		Iterator<String> bookApiIdsIterator = bookshelf.getBookApiIds().listIterator();
		while (bookApiIdsIterator.hasNext())
		{
			String bookApiId = bookApiIdsIterator.next();
			Map<String, Object> basic = googleBooksService.findBasicByApiId(bookApiId);
			basic.put("apiId", bookApiId);
			Double score = bookService.findScoreByApiId(bookApiId);
			if (score != null)
			{
				score = ((double) Math.round(score*10d))/10d;
				basic.put("score", score);
			}
			else
			{
				basic.put("score", null);
			}
			bookCards.add(basic);
		}
		return bookCards;
	}
	
	public Bookshelf bookshelfDeleteById(Long id) throws Exception
	{
		Bookshelf bookshelf = bookshelfService.deleteById(id);
		return bookshelf;
	}
}