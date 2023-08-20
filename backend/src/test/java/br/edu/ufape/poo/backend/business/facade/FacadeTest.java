package br.edu.ufape.poo.backend.business.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.data.AccountRepository;
import br.edu.ufape.poo.backend.data.BookshelfRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class FacadeTest
{
	@Autowired
	private Facade facade;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BookshelfRepository bookshelfRepository;
	
	@Test
	void accountSignUpValid() throws Exception
	{
		Account account = new Account();
		account.setName("a");
		account.setEmail("a@a.a");
		account.setPassword("a");
		facade.accountSignUp(account);
	}
	
	@Test
	void accountLogInValid() throws Exception
	{
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		accountRepository.save(account);
		facade.accountLogIn("a@a.a", "a");
	}
	
	@Test
	void accountUpdateValid() throws Exception
	{
		Account account = new Account();
		account.setName("a");
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		account.setName("b");
		facade.accountUpdate(account, "a@a.a", "a");
	}
	
	@Test
	void accountDeleteByIdValid() throws Exception
	{
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		facade.accountDeleteById(account.getId(), "a@a.a", "a");
	}
	
	@Test
	void accountFindOwnByIdValid() throws Exception
	{
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		facade.accountFindOwnById(account.getId(), "a@a.a", "a");
	}
	
	@Test
	void accountFindByIdValid() throws Exception
	{
		Account account = new Account();
		account = accountRepository.save(account);
		facade.accountFindById(account.getId());
	}
	
	@Test
	void bookshelfCreateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setName("a");
		bookshelf.setDescription("a");
		facade.bookshelfCreate(bookshelf, "a@a.a", "a");
	}
	
	@Test
	void bookshelfUpdateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setName("a");
		bookshelf.setDescription("a");
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		bookshelf.setName("b");
		facade.bookshelfUpdate(bookshelf, "a@a.a", "a");
	}
	
	@Test
	void bookshelfAddBookApiIdByIdValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		facade.bookshelfAddBookApiIdById(bookshelf.getId(), "u8w_DwAAQBAJ", "a@a.a", "a");
	}
	
	@Test
	void bookshelfRemoveBookApiIdByIdValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		List<String> bookApiIds = new ArrayList<String>();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookApiIds.add("u8w_DwAAQBAJ");
		bookshelf.setBookApiIds(bookApiIds);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		facade.bookshelfRemoveBookApiIdById(bookshelf.getId(), "u8w_DwAAQBAJ", "a@a.a", "a");
	}
	
	@Test
	void bookshelfDeleteByIdValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		facade.bookshelfDeleteById(bookshelf.getId(), "a@a.a", "a");
	}
	
	@Test
	void bookshelfFindOwnByIdValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		facade.bookshelfFindOwnById(bookshelf.getId(), "a@a.a", "a");
	}
	
	@Test
	void bookshelfFindByIdValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		facade.bookshelfFindById(bookshelf.getId());
	}
	
	@Test
	void bookshelfFindOwnByOwnerIdPaginateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> bookshelves = facade.bookshelfFindOwnByOwnerIdPaginate(account.getId(), 0, 10, "a@a.a", "a");
		assertEquals(bookshelves.size(), 1);
	}
	
	@Test
	void bookshelfFindByOwnerIdPaginateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> bookshelves = facade.bookshelfFindByOwnerIdPaginate(account.getId(), 0, 10);
		assertEquals(bookshelves.size(), 1);
	}
	
	@Test
	void bookshelfFindOwnBooksByIdPaginateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		List<String> bookApiIds = new ArrayList<String>();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookApiIds.add("u8w_DwAAQBAJ");
		bookshelf.setBookApiIds(bookApiIds);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> books = facade.bookshelfFindOwnBooksByIdPaginate(bookshelf.getId(), 0, 10, "a@a.a", "a");
		assertEquals(books.size(), 1);
	}
	
	@Test
	void bookshelfFindBooksByIdPaginateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		List<String> bookApiIds = new ArrayList<String>();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		bookApiIds.add("u8w_DwAAQBAJ");
		bookshelf.setBookApiIds(bookApiIds);
		bookshelf.setPrivacy(true);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> books = facade.bookshelfFindBooksByIdPaginate(bookshelf.getId(), 0, 10);
		assertEquals(books.size(), 1);
	}
	
	@Test
	void bookshelfFindOwnSelectsByIdPaginateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		List<String> bookApiIds = new ArrayList<String>();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookApiIds.add("u8w_DwAAQBAJ");
		bookshelf.setBookApiIds(bookApiIds);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> selects = facade.bookshelfFindOwnSelectByOwnerId(account.getId(), "u8w_DwAAQBAJ", "a@a.a", "a");
		assertEquals(selects.size(), 1);
	}
	
	@Test
	void bookshelfFindSelectsByIdPaginateValid() throws Exception
	{
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		List<String> bookApiIds = new ArrayList<String>();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		bookApiIds.add("u8w_DwAAQBAJ");
		bookshelf.setBookApiIds(bookApiIds);
		bookshelf.setPrivacy(true);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> selects = facade.bookshelfFindSelectByOwnerId(account.getId(), "u8w_DwAAQBAJ");
		assertEquals(selects.size(), 1);
	}
}