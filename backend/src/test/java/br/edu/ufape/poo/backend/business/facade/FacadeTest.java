package br.edu.ufape.poo.backend.business.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.business.entity.Review;
import br.edu.ufape.poo.backend.data.AccountRepository;
import br.edu.ufape.poo.backend.data.BookshelfRepository;
import br.edu.ufape.poo.backend.data.ReviewRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class FacadeTest {
	@Autowired
	private Facade facade;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BookshelfRepository bookshelfRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	// ACCOUNT
	
	@Test
	void accountSignUpValid() throws Exception {
		Account account = new Account();
		account.setName("a");
		account.setEmail("a@a.a");
		account.setPassword("a");
		facade.accountSignUp(account);
	}

	@Test
	void accountLogInValid() throws Exception {
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		accountRepository.save(account);
		facade.accountLogIn("a@a.a", "a");
	}

	@Test
	void accountUpdateValid() throws Exception {
		Account account = new Account();
		account.setName("a");
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		account.setName("b");
		facade.accountUpdate(account, "a@a.a", "a");
	}

	@Test
	void accountDeleteByIdValid() throws Exception {
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		facade.accountDeleteById(account.getId(), "a@a.a", "a");
	}

	@Test
	void accountFindOwnByIdValid() throws Exception {
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		facade.accountFindOwnById(account.getId(), "a@a.a", "a");
	}

	@Test
	void accountFindByIdValid() throws Exception {
		Account account = new Account();
		account = accountRepository.save(account);
		facade.accountFindById(account.getId());
	}

	// BOOKSHELF
	
	@Test
	void bookshelfCreateValid() throws Exception {
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
	void bookshelfUpdateValid() throws Exception {
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
	void bookshelfAddBookApiIdByIdValid() throws Exception {
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
	void bookshelfRemoveBookApiIdByIdValid() throws Exception {
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
	void bookshelfDeleteByIdValid() throws Exception {
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
	void bookshelfFindOwnByIdValid() throws Exception {
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
	void bookshelfFindByIdValid() throws Exception {
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		facade.bookshelfFindById(bookshelf.getId());
	}

	@Test
	void bookshelfFindOwnByOwnerIdPaginateValid() throws Exception {
		Account account = new Account();
		Bookshelf bookshelf = new Bookshelf();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		bookshelf.setBookApiIds(new ArrayList<String>());
		bookshelf.setOwner(account);
		bookshelf = bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> bookshelves = facade.bookshelfFindOwnByOwnerIdPaginate(account.getId(), 0, 10,
				"a@a.a", "a");
		assertEquals(bookshelves.size(), 1);
	}

	@Test
	void bookshelfFindByOwnerIdPaginateValid() throws Exception {
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
	void bookshelfFindOwnBooksByIdPaginateValid() throws Exception {
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
	void bookshelfFindBooksByIdPaginateValid() throws Exception {
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
	void bookshelfFindOwnSelectsByIdPaginateValid() throws Exception {
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
	void bookshelfFindSelectsByIdPaginateValid() throws Exception {
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

	// BOOK

	@Test
	void findBookByApiIdValid() throws Exception {
		String bookApiId = "u8w_DwAAQBAJ";
		Map<String, Object> results = facade.bookFindByApiId(bookApiId, "complete");
		assertFalse(results.isEmpty());
	}

	@Test
	void advancedSearchBook() throws Exception {
		List<Map<String, Object>> results = new ArrayList<>();
		results = facade.bookFindByAdvanced(null, null, "shakespeare", null, null, null, 0, 1, "Jane", null, "book");
		assertFalse(results.isEmpty());
	}

	@Test
	void advancedSearchBookshelf() throws Exception {
		Account account = new Account();
		account.setName("Jane");
		accountRepository.save(account);
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setName("Favorites");
		bookshelf.setOwner(account);
		bookshelfRepository.save(bookshelf);
		List<Map<String, Object>> results = facade.bookFindByAdvanced(null, null, null, null, null, null, 0, 1, "Jane", "Favorites", "bookshelf");
		assertFalse(results.isEmpty());
		assertEquals(results.size(), 1);

		for (int i = 0; i < results.size(); i++) {
			Object result = results.get(i);
			assertTrue(result instanceof Bookshelf);
			Bookshelf foundBookshelf = (Bookshelf) result;
			assertNotNull(foundBookshelf.getName());
			assertNotNull(foundBookshelf.getOwner());
			assertTrue(foundBookshelf.getOwner().getName().contains("Jane"));
			assertEquals("Favorites", foundBookshelf.getName());
		}
	}

	@Test
	void advancedSearchAll() throws Exception {
		List<Map<String, Object>> results = facade.bookFindByAdvanced(null, "Hamlet", "Shakespeare", null, null, null, 0, 1,"Jane", "Favorites", "all");
		assertFalse(results.isEmpty());

		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> result = results.get(i);
			if (result.containsKey("name")) {
				Bookshelf bookshelf = (Bookshelf) result;
				assertNotNull(bookshelf.getName());
				assertNotNull(bookshelf.getOwner());
				assertTrue(bookshelf.getOwner().getName().contains("Jane"));
				assertEquals("Favorites", bookshelf.getName());

			} else if (result.containsKey("title")) {
				Map<?, ?> book = (Map<?, ?>) result;
				Object title = book.get("title");
				if (title instanceof String) {
					assertEquals(true, ((String) title).contains("Hamlet"));
				}
				Object authors = book.get("authors");
				if (authors instanceof List) {
					List<?> authorsList = (List<?>) authors;
					for (int j = 0; j < authorsList.size(); j++) {
						Object author = authorsList.get(j);
						if (author instanceof String) {
							String authorName = (String) author;
							assertEquals(true, ((String) authorName).contains("Shakespeare"));
						}
					}
				}
			}
		}
	}

	// REVIEW
	
	@Test
	void reviewCreateValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		review.setBookScore(3.5);
		review.setText("aaaaaaaaaaaaa");
		review.setBookApiId("u8w_DwAAQBAJ");
		facade.reviewCreate(review, "a@a.a", "a");
	}

	@Test
	void reviewUpdateValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		review.setBookScore(3.5);
		review.setText("aaaaaaaaaaaaa");
		review.setBookApiId("u8w_DwAAQBAJ");
		review.setOwner(account);
		review = facade.reviewCreate(review, "a@a.a", "a");
		review.setText("bbbbbbbbbbbbbbb");
		facade.reviewUpdate(review, "a@a.a", "a");
	}

	@Test
	void reviewDeleteByApiIdValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		review.setBookScore(3.5);
		review.setText("aaaaaaaaaaaaa");
		review.setBookApiId("u8w_DwAAQBAJ");
		review.setOwner(account);
		review = facade.reviewCreate(review, "a@a.a", "a");
		facade.reviewDeleteById(review.getId(), "a@a.a", "a");
	}

	@Test
	void reviewFindByOwnerIdPaginateValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		review.setBookApiId("u8w_DwAAQBAJ");
		review.setPrivacy(true);
		review.setOwner(account);
		review = reviewRepository.save(review);
		List<Map<String, Object>> reviews = facade.reviewFindByOwnerIdPaginate(review.getOwner().getId(), 0, 10);
		assertEquals(reviews.size(), 1);
	}

	@Test
	void reviewFindOwnByOwnerIdPaginateValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		review.setOwner(account);
		review.setBookApiId("u8w_DwAAQBAJ");
		review = reviewRepository.save(review);
		List<Map<String, Object>> reviews = facade.reviewFindOwnByOwnerIdPaginate(review.getOwner().getId(), 0, 10, "a@a.a", "a");
		assertEquals(reviews.size(), 1);
	}

	@Test
	void reviewFindByBookApiIdPaginateValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setPrivacy(true);
		account = accountRepository.save(account);
		review.setBookApiId("u8w_DwAAQBAJ");
		review.setPrivacy(true);
		review.setOwner(account);
		review = reviewRepository.save(review);
		List<Review> reviews = facade.reviewFindByBookApiIdPaginate(review.getBookApiId(), 0, 10);
		assertEquals(reviews.size(), 1);
	}

	@Test
	void reviewFindByBookApiIdPaginateAutenticadedValid() throws Exception {
		Account account = new Account();
		Review review = new Review();
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		review.setOwner(account);
		review.setBookApiId("u8w_DwAAQBAJ");
		review = reviewRepository.save(review);
		List<Review> reviews = facade.reviewFindByBookApiIdPaginateAutenticaded(review.getBookApiId(), 0, 10, "a@a.a", "a");
		assertEquals(reviews.size(), 1);
	}

}