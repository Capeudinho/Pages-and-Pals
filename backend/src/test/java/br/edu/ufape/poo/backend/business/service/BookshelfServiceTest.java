package br.edu.ufape.poo.backend.business.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.data.AccountRepository;
import br.edu.ufape.poo.backend.data.BookshelfRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class BookshelfServiceTest {
	@Autowired
	private BookshelfServiceInterface bookshelfService;
	@Autowired
	private BookshelfRepository bookshelfRepository;
	@Autowired
	private AccountRepository accountRepository;

	@Test
	void createValid() throws Exception {
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setName("a");
		bookshelf.setDescription("a");
		bookshelfService.create(bookshelf);
	}

	@Test
	void updateValid() throws Exception {
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setName("a");
		bookshelf.setDescription("a");
		bookshelf = bookshelfRepository.save(bookshelf);
		bookshelf.setName("b");
		bookshelfService.update(bookshelf);
	}

	@Test
	void addBookApiIdByIdValid() throws Exception {
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf = bookshelfRepository.save(bookshelf);
		bookshelfService.addBookApiIdById(bookshelf.getId(), "u8w_DwAAQBAJ");
	}

	@Test
	void addMultipleBookApiIdByIdValid() throws Exception {
		Bookshelf bookshelf1 = new Bookshelf();
		bookshelf1.setBookApiIds(new ArrayList<String>());
		bookshelf1 = bookshelfRepository.save(bookshelf1);
		bookshelfService.addBookApiIdById(bookshelf1.getId(), "u8w_DwAAQBAJ");
		bookshelfService.addBookApiIdById(bookshelf1.getId(), "uCs-1AAAACAAJ");
	}

	@Test
	void removeBookApiIdByIdValid() throws Exception {
		Bookshelf bookshelf = new Bookshelf();
		List<String> bookApiIds = new ArrayList<String>();
		bookApiIds.add("u8w_DwAAQBAJ");
		bookshelf.setBookApiIds(bookApiIds);
		bookshelf = bookshelfRepository.save(bookshelf);
		bookshelfService.removeBookApiIdById(bookshelf.getId(), "u8w_DwAAQBAJ");
	}

	@Test
	void deleteByIdValid() throws Exception {
		Bookshelf bookshelf = new Bookshelf();
		bookshelf = bookshelfRepository.save(bookshelf);
		bookshelfService.deleteById(bookshelf.getId());
	}

	@Test
	void findByOwnerIdValid() throws Exception {
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelfRepository.save(bookshelf);
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerId(account.getId());
		assertEquals(bookshelves.size(), 1);
	}

	@Test
	void findByOwnerIdPaginateValid() throws Exception {
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelfRepository.save(bookshelf);
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerIdPaginate(account.getId(), 0, 10);
		assertEquals(bookshelves.size(), 1);
	}

	@Test
	void findByIdValid() throws Exception {
		Bookshelf bookshelf = new Bookshelf();
		bookshelf = bookshelfRepository.save(bookshelf);
		bookshelfService.findById(bookshelf.getId());
	}

	@Test
	void countByOwnerIdPaginateValid() throws Exception {
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelfRepository.save(bookshelf);
		int bookshelfCount = bookshelfService.countByOwnerId(account.getId());
		assertEquals(bookshelfCount, 1);
	}

	public void testFindByOwnerAndBookshelfName() {
		Account user1 = new Account();
		Bookshelf bookshelf1 = new Bookshelf();
		Bookshelf bookshelf2 = new Bookshelf();
		user1.setName("A");
		user1.setPrivacy(true);
		user1 = accountRepository.save(user1);
		bookshelf1.setOwner(user1);
		bookshelf2.setOwner(user1);
		bookshelf1.setName("Bookshelf 1");
		bookshelf2.setName("Bookshelf 2");
		bookshelf1.setPrivacy(true);
		bookshelf2.setPrivacy(true);
		bookshelfRepository.save(bookshelf1);
		bookshelfRepository.save(bookshelf2);
		List<String> bookApiIds = new ArrayList<String>();
		List<Bookshelf> results = bookshelfRepository.findByAdvanced(user1.getName(), "Bookshelf 1", bookApiIds, null);
		assertEquals(results.size(), 1);
	}

	@Test
	public void testFindByBothNullParameters() {
		Account user1 = new Account();
		Bookshelf bookshelf1 = new Bookshelf();
		Bookshelf bookshelf2 = new Bookshelf();
		user1.setName("A");
		user1 = accountRepository.save(user1);
		bookshelf1.setOwner(user1);
		bookshelf2.setOwner(user1);
		bookshelf1.setName("Bookshelf 1");
		bookshelf2.setName("Bookshelf 2");
		bookshelf1.setPrivacy(true);
		bookshelf2.setPrivacy(true);
		bookshelfRepository.save(bookshelf1);
		bookshelfRepository.save(bookshelf2);
		List<String> bookApiIds = new ArrayList<String>();
		List<Bookshelf> results = bookshelfRepository.findByAdvanced(null, null, bookApiIds, null);
		assertEquals(results.size(), 0);
	}

	@Test
	public void testFindByNullOwnerName() {
		Account user1 = new Account();
		Bookshelf bookshelf1 = new Bookshelf();
		Bookshelf bookshelf2 = new Bookshelf();
		user1.setName("A");
		user1.setPrivacy(true);
		user1 = accountRepository.save(user1);
		bookshelf1.setOwner(user1);
		bookshelf2.setOwner(user1);
		bookshelf1.setName("Bookshelf 1");
		bookshelf2.setName("Bookshelf 2");
		bookshelf1.setPrivacy(true);
		bookshelf2.setPrivacy(true);
		bookshelfRepository.save(bookshelf1);
		bookshelfRepository.save(bookshelf2);
		List<String> bookApiIds = new ArrayList<String>();
		List<Bookshelf> results = bookshelfRepository.findByAdvanced(null, "Bookshelf 1", bookApiIds, null);
		assertEquals(results.size(), 1);
	}

	@Test
	public void testFindByNullBookshelfName() {
		Account user1 = new Account();
		Bookshelf bookshelf1 = new Bookshelf();
		Bookshelf bookshelf2 = new Bookshelf();
		user1.setName("A");
		user1.setPrivacy(true);
		user1 = accountRepository.save(user1);
		bookshelf1.setOwner(user1);
		bookshelf2.setOwner(user1);
		bookshelf1.setName("Bookshelf 1");
		bookshelf2.setName("Bookshelf 2");
		bookshelf1.setPrivacy(true);
		bookshelf2.setPrivacy(true);
		bookshelfRepository.save(bookshelf1);
		bookshelfRepository.save(bookshelf2);
		List<String> bookApiIds = new ArrayList<String>();
		List<Bookshelf> results = bookshelfRepository.findByAdvanced(user1.getName(), null, bookApiIds, null);
		assertEquals(results.size(), 2);
	}

	@Test
	public void testFindByNonExistingOwner() {
		Account user1 = new Account();
		Bookshelf bookshelf1 = new Bookshelf();
		Bookshelf bookshelf2 = new Bookshelf();
		user1.setName("A");
		user1 = accountRepository.save(user1);
		bookshelf1.setOwner(user1);
		bookshelf2.setOwner(user1);
		bookshelf1.setName("Bookshelf 1");
		bookshelf2.setName("Bookshelf 2");
		bookshelf1.setPrivacy(true);
		bookshelf2.setPrivacy(true);
		bookshelfRepository.save(bookshelf1);
		bookshelfRepository.save(bookshelf2);
		List<String> bookApiIds = new ArrayList<String>();
		List<Bookshelf> results = bookshelfRepository.findByAdvanced("Nonexistent User", "Bookshelf 1", bookApiIds, null);
		assertEquals(0, results.size());
	}

	@Test
	public void testFindByOwnerAndNonExistingBookshelfName() {
		Account user1 = new Account();
		Bookshelf bookshelf1 = new Bookshelf();
		Bookshelf bookshelf2 = new Bookshelf();
		user1.setName("A");
		user1 = accountRepository.save(user1);
		bookshelf1.setOwner(user1);
		bookshelf2.setOwner(user1);
		bookshelf1.setName("Bookshelf 1");
		bookshelf2.setName("Bookshelf 2");
		bookshelf1.setPrivacy(true);
		bookshelf2.setPrivacy(true);
		bookshelfRepository.save(bookshelf1);
		bookshelfRepository.save(bookshelf2);
		List<String> bookApiIds = new ArrayList<String>();
		List<Bookshelf> results = bookshelfRepository.findByAdvanced(user1.getName(),"Nonexistent Bookshelf", bookApiIds, null);
		assertEquals(0, results.size());
	}

}
