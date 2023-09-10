package br.edu.ufape.poo.backend.business.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.business.entity.Book;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.business.entity.Review;
import br.edu.ufape.poo.backend.business.service.AccountServiceInterface;
import br.edu.ufape.poo.backend.business.service.BookServiceInterface;
import br.edu.ufape.poo.backend.business.service.BookshelfServiceInterface;
import br.edu.ufape.poo.backend.business.service.GoogleBooksServiceInterface;
import br.edu.ufape.poo.backend.business.service.ReviewServiceInterface;
import br.edu.ufape.poo.backend.exceptions.AccessDeniedException;

@Service
public class Facade {
	@Autowired
	private AccountServiceInterface accountService;
	@Autowired
	private BookServiceInterface bookService;
	@Autowired
	private BookshelfServiceInterface bookshelfService;
	@Autowired
	private GoogleBooksServiceInterface googleBooksService;
	@Autowired
	private ReviewServiceInterface reviewService;

	// ACCOUNT

	public Map<String, Object> accountSignUp(Account account) throws Exception {
		Account newAccount = accountService.signUp(account);
		Map<String, Object> newAccountProfile = new HashMap<>();
		Bookshelf read = new Bookshelf();
		Bookshelf reading = new Bookshelf();
		Bookshelf favorites = new Bookshelf();
		read.setName("Read");
		read.setDescription("Books I have read.");
		read.setPrivacy(true);
		read.setOwner(newAccount);
		reading.setName("Reading");
		reading.setDescription("Books I am reading.");
		reading.setPrivacy(true);
		reading.setOwner(newAccount);
		favorites.setName("Favorites");
		favorites.setDescription("Books I like the most.");
		favorites.setPrivacy(true);
		favorites.setOwner(newAccount);
		bookshelfService.create(read);
		bookshelfService.create(reading);
		bookshelfService.create(favorites);
		newAccountProfile.put("id", newAccount.getId());
		newAccountProfile.put("name", newAccount.getName());
		newAccountProfile.put("email", newAccount.getEmail());
		newAccountProfile.put("password", newAccount.getPassword());
		newAccountProfile.put("biography", newAccount.getBiography());
		newAccountProfile.put("picture", newAccount.getPicture());
		newAccountProfile.put("privacy", newAccount.isPrivacy());
		return newAccountProfile;
	}

	public Map<String, Object> accountLogIn(String email, String password) throws Exception {
		Account account = accountService.logIn(email, password);
		Map<String, Object> accountProfile = new HashMap<>();
		accountProfile.put("id", account.getId());
		accountProfile.put("name", account.getName());
		accountProfile.put("email", account.getEmail());
		accountProfile.put("password", account.getPassword());
		accountProfile.put("biography", account.getBiography());
		accountProfile.put("picture", account.getPicture());
		accountProfile.put("privacy", account.isPrivacy());
		return accountProfile;
	}

	public Map<String, Object> accountUpdate(Account account, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != account.getId()) {
			throw new AccessDeniedException();
		}
		Account newAccount = accountService.update(account);
		Map<String, Object> newAccountProfile = new HashMap<>();
		newAccountProfile.put("id", newAccount.getId());
		newAccountProfile.put("name", newAccount.getName());
		newAccountProfile.put("email", newAccount.getEmail());
		newAccountProfile.put("password", newAccount.getPassword());
		newAccountProfile.put("biography", newAccount.getBiography());
		newAccountProfile.put("picture", newAccount.getPicture());
		newAccountProfile.put("privacy", newAccount.isPrivacy());
		return newAccountProfile;
	}

	public Map<String, Object> accountDeleteById(long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != id) {
			throw new AccessDeniedException();
		}
		bookshelfService.deleteByOwnerId(id);
		List<Review> reviews = reviewService.findByOwnerId(id);
		for(int index = 0; index < reviews.size(); index++)
		{
			reviewDeleteByIdUtility(reviews.get(index));
		}
		Account oldAccount = accountService.deleteById(id);
		Map<String, Object> oldAccountProfile = new HashMap<>();
		oldAccountProfile.put("id", oldAccount.getId());
		oldAccountProfile.put("name", oldAccount.getName());
		oldAccountProfile.put("email", oldAccount.getEmail());
		oldAccountProfile.put("password", oldAccount.getPassword());
		oldAccountProfile.put("biography", oldAccount.getBiography());
		oldAccountProfile.put("picture", oldAccount.getPicture());
		oldAccountProfile.put("privacy", oldAccount.isPrivacy());
		return oldAccountProfile;
	}

	public Map<String, Object> accountFindOwnById(long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != id) {
			throw new AccessDeniedException();
		}
		Map<String, Object> accountProfile = accountFindByIdUtility(requestingAccount, true);
		return accountProfile;
	}

	public Map<String, Object> accountFindById(long id) throws Exception {
		Account account = accountService.findById(id);
		Map<String, Object> accountProfile = accountFindByIdUtility(account, false);
		return accountProfile;
	}

	// BOOKSHELF

	public Bookshelf bookshelfCreate(Bookshelf bookshelf, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		bookshelf.setOwner(requestingAccount);
		Bookshelf newBookshelf = bookshelfService.create(bookshelf);
		return newBookshelf;
	}

	public Bookshelf bookshelfUpdate(Bookshelf bookshelf, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf oldBookshelf = bookshelfService.findById(bookshelf.getId());
		if (requestingAccount.getId() != oldBookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.update(bookshelf);
		return newBookshelf;
	}

	public Bookshelf bookshelfAddBookApiIdById(long id, String apiId, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.addBookApiIdById(id, apiId);
		return newBookshelf;
	}

	public Bookshelf bookshelfRemoveBookApiIdById(long id, String apiId, String email, String password)
			throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.removeBookApiIdById(id, apiId);
		return newBookshelf;
	}

	public Bookshelf bookshelfDeleteById(long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf oldBookshelf = bookshelfService.deleteById(id);
		return oldBookshelf;
	}

	public Map<String, Object> bookshelfFindOwnById(long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Map<String, Object> bookshelfCard = bookshelfFindByIdUtility(bookshelf, true);
		return bookshelfCard;
	}

	public Map<String, Object> bookshelfFindById(long id) throws Exception {
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (!bookshelf.getOwner().isPrivacy()) {
			throw new AccessDeniedException();
		}
		Map<String, Object> bookshelfCard = bookshelfFindByIdUtility(bookshelf, false);
		return bookshelfCard;
	}

	public List<Map<String, Object>> bookshelfFindOwnByOwnerIdPaginate(long ownerId, int offset, int limit,
			String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != ownerId) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfCards = bookshelfFindByOwnerIdPaginateUtility(requestingAccount, offset,
				limit, true);
		return bookshelfCards;
	}

	public List<Map<String, Object>> bookshelfFindByOwnerIdPaginate(long ownerId, int offset, int limit)
			throws Exception {
		Account account = accountService.findById(ownerId);
		if (!account.isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfCards = bookshelfFindByOwnerIdPaginateUtility(account, offset, limit, false);
		return bookshelfCards;
	}

	public List<Map<String, Object>> bookshelfFindOwnBooksByIdPaginate(long id, int offset, int limit, String email,
			String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> books = bookshelfFindBooksByIdPaginateUtility(bookshelf, offset, limit);
		return books;
	}

	public List<Map<String, Object>> bookshelfFindBooksByIdPaginate(long id, int offset, int limit) throws Exception {
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (!bookshelf.isPrivacy() || !bookshelf.getOwner().isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> books = bookshelfFindBooksByIdPaginateUtility(bookshelf, offset, limit);
		return books;
	}

	public List<Map<String, Object>> bookshelfFindOwnSelectByOwnerId(long ownerId, String apiId, String email,
			String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != ownerId) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfSelects = bookshelfFindSelectByOwnerIdUtility(requestingAccount, apiId,
				true);
		return bookshelfSelects;
	}

	public List<Map<String, Object>> bookshelfFindSelectByOwnerId(long ownerId, String apiId) throws Exception {
		Account account = accountService.findById(ownerId);
		if (!account.isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfSelects = bookshelfFindSelectByOwnerIdUtility(account, apiId, false);
		return bookshelfSelects;
	}

	// BOOK

	public Map<String, Object> bookFindByApiId(String apiId, String extractInfo) throws Exception {
		Map<String, Object> result = googleBooksService.findByApiId(apiId, extractInfo);
		Double score = bookService.findScoreByApiId((String) result.get("apiId"));
		int reviewCount = reviewService.countByBookApiId(apiId);
		if (score != null) {
			score = ((double) Math.round(score * 10d)) / 10d;
			result.put("score", score);
		} else {
			result.put("score", null);
		}
		result.put("reviewCount", reviewCount);
		return result;
	}

	public List<Map<String, Object>> bookFindByAdvancedAuthenticated(String term, String title, String author, String subject,
			String publisher, String isbn, int offset, int limit, String ownerName, String bookshelfName,
			String resultType, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		List<Map<String, Object>> results = new ArrayList<>();
		results = bookFindByAdvancedUtility(term, title, author, subject, publisher, isbn, offset, limit, ownerName,
				bookshelfName, resultType, requestingAccount);
		return results;
	}

	public List<Map<String, Object>> bookFindByAdvanced(String term, String title, String author, String subject,
			String publisher, String isbn, int offset, int limit, String ownerName, String bookshelfName,
			String resultType) throws Exception {
		List<Map<String, Object>> results = new ArrayList<>();
		results = bookFindByAdvancedUtility(term, title, author, subject, publisher, isbn, offset, limit, ownerName,
				bookshelfName, resultType, null);
		return results;
	}

	// REVIEW

	// Verificando se a conta criadora da Review existe
	public Review reviewCreate(Review review, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		review.setOwner(requestingAccount);
		Book book = bookService.findByApiId(review.getBookApiId());
		if (book == null && review.getBookScore() != null) {
			googleBooksService.findByApiId(review.getBookApiId(), "incomplete");
		}
		Review newReview = reviewService.create(review);
		if (newReview.getBookScore() != null) {
			if (book == null) {
				book = new Book();
				book.setApiId(newReview.getBookApiId());
				book = bookService.create(book);
			}
			Double bookScore = book.getScoreTotal() + newReview.getBookScore();
			book.setScoreTotal(bookScore);
			int reviewCount = book.getReviewCount() + 1;
			book.setReviewCount(reviewCount);
			bookService.update(book);
		}
		return newReview;
	}

	// Verificando se a conta que quer editar existe e se é criadora da Review
	public Review reviewUpdate(Review review, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Review oldReview = reviewService.findById(review.getId());
		Double oldBookScore = oldReview.getBookScore();
		if (requestingAccount.getId() != oldReview.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Review newReview = reviewService.update(review);
		Book book = bookService.findByApiId(oldReview.getBookApiId());
		Double bookScore;
		int reviewCount;
		if (oldBookScore != null) {
			bookScore = book.getScoreTotal() - oldBookScore;
			book.setScoreTotal(bookScore);
			reviewCount = book.getReviewCount() - 1;
			book.setReviewCount(reviewCount);
			if (book.getReviewCount() == 0 && newReview.getBookScore() == null) {
				bookService.deleteByApiId(book.getApiId());
			} else {
				bookService.update(book);
			}
		}
		if (newReview.getBookScore() != null) {
			if (book == null) {
				book = new Book();
				book.setApiId(newReview.getBookApiId());
				book = bookService.create(book);
			}
			bookScore = book.getScoreTotal() + newReview.getBookScore();
			book.setScoreTotal(bookScore);
			reviewCount = book.getReviewCount() + 1;
			book.setReviewCount(reviewCount);
			bookService.update(book);
		}
		return newReview;
	}

	// Verificando se a conta que quer deletar existe e se é criadora da Review
	public Review reviewDeleteById(long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Review review = reviewService.findById(id);
		if (requestingAccount.getId() != review.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Review oldReview = reviewDeleteByIdUtility(review);
		return oldReview;
	}
	
	public Review reviewFindById(long id) throws Exception {
		Review review = reviewService.findById(id);
		if(review.isPrivacy() == false || review.getOwner().isPrivacy() == false) {
			throw new AccessDeniedException();
		}
		return review;
	}
	
	public Review reviewFindOwnById (long id, String email, String password) throws Exception {
		Review review = reviewService.findById(id);
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != review.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		
		return review;
	}

	// Buscar lista de Reviews de um usuário qualquer paginado
	public List<Map<String, Object>> reviewFindByOwnerIdPaginate(long ownerId, int offset, int limit) throws Exception {
		Account account = accountService.findById(ownerId);
		if (!account.isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> reviews = reviewFindByOwnerIdPaginateUtility(ownerId, offset, limit, false);
		return reviews;
	}

	// Buscar lista de Reviews do prório usuário paginado
	public List<Map<String, Object>> reviewFindOwnByOwnerIdPaginate(long ownerId, int offset, int limit, String email,
			String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != ownerId) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> reviews = reviewFindByOwnerIdPaginateUtility(ownerId, offset, limit, true);
		return reviews;
	}

	// Bucar lista de reviews pelo Id do livro sem autenticação
	public List<Review> reviewFindByBookApiIdPaginate(String bookApiId, int offset, int limit) {
		List<Review> reviews = reviewFindByBookApiIdPaginateUtility(bookApiId, 0L, offset, limit, false);
		return reviews;
	}

	// Bucar lista de reviews pelo Id do livro com autenticação
	public List<Review> reviewFindByBookApiIdPaginateAutenticaded(String bookApiId, int offset, int limit, String email,
			String password) throws Exception {
		Account account = accountService.authenticate(email, password);
		List<Review> reviews = reviewFindByBookApiIdPaginateUtility(bookApiId, account.getId(), offset, limit, true);
		return reviews;
	}

	// UTILITY

	private Map<String, Object> accountFindByIdUtility(Account account, boolean complete) throws Exception {
		Map<String, Object> accountProfile = new HashMap<>();
		accountProfile.put("id", account.getId());
		accountProfile.put("name", account.getName());
		accountProfile.put("picture", account.getPicture());
		accountProfile.put("privacy", account.isPrivacy());
		if (account.isPrivacy() || complete) {
			int bookshelfCount = bookshelfService.countByOwnerId(account.getId());
			int reviewCount = reviewService.countByOwnerId(account.getId());
			accountProfile.put("email", account.getEmail());
			accountProfile.put("biography", account.getBiography());
			accountProfile.put("bookshelfCount", bookshelfCount);
			accountProfile.put("reviewCount", reviewCount);
		}
		return accountProfile;
	}

	private Map<String, Object> bookshelfFindByIdUtility(Bookshelf bookshelf, boolean complete) throws Exception {
		Map<String, Object> bookshelfCard = new HashMap<>();
		bookshelfCard.put("id", bookshelf.getId());
		bookshelfCard.put("name", bookshelf.getName());
		bookshelfCard.put("privacy", bookshelf.isPrivacy());
		bookshelfCard.put("owner", bookshelf.getOwner());
		if (bookshelf.isPrivacy() || complete) {
			List<String> covers = new ArrayList<String>();
			for (int index = 0; index < bookshelf.getBookApiIds().size() && index < 3; index++) {
				String bookApiId = bookshelf.getBookApiIds().get(index);
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

	private List<Map<String, Object>> bookshelfFindByOwnerIdPaginateUtility(Account account, int offset, int limit,
			boolean complete) throws Exception {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 1) {
			limit = 1;
		}
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerIdPaginate(account.getId(), offset, limit);
		List<Map<String, Object>> bookshelfCards = bookshelfExtractCards(bookshelves, account);
		return bookshelfCards;
	}

	private List<Map<String, Object>> bookshelfFindBooksByIdPaginateUtility(Bookshelf bookshelf, int offset, int limit)
			throws Exception {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 1) {
			limit = 1;
		}
		if (offset > bookshelf.getBookApiIds().size()) {
			offset = bookshelf.getBookApiIds().size();
		}
		if (limit > bookshelf.getBookApiIds().size() - offset) {
			limit = bookshelf.getBookApiIds().size() - offset;
		}
		List<Map<String, Object>> books = new ArrayList<Map<String, Object>>();
		List<String> bookApiIds = bookshelf.getBookApiIds().subList(offset, offset + limit);
		for (int index = 0; index < bookApiIds.size(); index++) {
			String bookApiId = bookApiIds.get(index);
			Map<String, Object> book = googleBooksService.findByApiId(bookApiId, "incomplete");
			book.put("apiId", bookApiId);
			Double score = bookService.findScoreByApiId(bookApiId);
			if (score != null) {
				score = ((double) Math.round(score * 10d)) / 10d;
				book.put("score", score);
			} else {
				book.put("score", null);
			}
			books.add(book);
		}
		return books;
	}

	private List<Map<String, Object>> bookshelfFindSelectByOwnerIdUtility(Account account, String apiId,
			boolean complete) throws Exception {
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerId(account.getId());
		List<Map<String, Object>> bookshelfSelects = new ArrayList<Map<String, Object>>();
		for (int index = 0; index < bookshelves.size(); index++) {
			Bookshelf bookshelf = bookshelves.get(index);
			if (bookshelf.isPrivacy() || complete) {
				Map<String, Object> bookshelfSelect = new HashMap<>();
				bookshelfSelect.put("id", bookshelf.getId());
				bookshelfSelect.put("name", bookshelf.getName());
				if (bookshelf.getBookApiIds().contains(apiId)) {
					bookshelfSelect.put("contains", true);
				} else {
					bookshelfSelect.put("contains", false);
				}
				bookshelfSelects.add(bookshelfSelect);
			}
		}
		return bookshelfSelects;
	}

	private List<Map<String, Object>> bookshelfExtractCards(List<Bookshelf> bookshelves, Account account) {
		List<Map<String, Object>> bookshelfCards = new ArrayList<Map<String, Object>>();
		Bookshelf bookshelf;
		for (int bookshelfIndex = 0; bookshelfIndex < bookshelves.size(); bookshelfIndex++) {
			bookshelf = bookshelves.get(bookshelfIndex);
			Map<String, Object> bookshelfCard = new HashMap<>();
			bookshelfCard.put("id", bookshelf.getId());
			bookshelfCard.put("name", bookshelf.getName());
			bookshelfCard.put("privacy", bookshelf.isPrivacy());
			bookshelfCard.put("owner", bookshelf.getOwner());
			if (bookshelf.isPrivacy() || (account != null && bookshelf.getOwner().getId() == account.getId())) {
				List<String> covers = new ArrayList<String>();
				for (int coverIndex = 0; coverIndex < bookshelf.getBookApiIds().size()
						&& coverIndex < 3; coverIndex++) {
					String bookApiId = bookshelf.getBookApiIds().get(coverIndex);
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

	private List<Map<String, Object>> bookFindByAdvancedUtility(String term, String title, String author, String subject,
			String publisher, String isbn, int offset, int limit, String ownerName, String bookshelfName,
			String resultType, Account account) throws Exception {
		List<Map<String, Object>> results = new ArrayList<>();
		List<Map<String, Object>> bookResults = new ArrayList<>();
		List<Map<String, Object>> bookshelfCards = new ArrayList<>();
		List<Bookshelf> bookshelfResults = new ArrayList<>();
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 1) {
			limit = 1;
		}
		if ("all".equals(resultType) || "book".equals(resultType)) {
			bookResults = googleBooksService.advancedSearchResults(term, title, author, subject, publisher, isbn,
					offset, limit, "incomplete");
			for (int index = 0; index < bookResults.size(); index++) {
				Double score = bookService.findScoreByApiId((String) bookResults.get(index).get("apiId"));
				if (score != null) {
					score = ((double) Math.round(score * 10d)) / 10d;
					bookResults.get(index).put("score", score);
				} else {
					bookResults.get(index).put("score", null);
				}
			}
			results.addAll(bookResults);
		}
		if ("all".equals(resultType) || "bookshelf".equals(resultType)) {
			if (account == null) {
				bookshelfResults = bookshelfService.findByPublicAndOwnerNameAndBookshelfName(ownerName, bookshelfName,
						offset, limit);
			} else {
				bookshelfResults = bookshelfService.findByPublicOrOwnerIdAndOwnerNameAndBookshelfName(ownerName,
						bookshelfName, account.getId(), offset, limit);
			}
			bookshelfCards = bookshelfExtractCards(bookshelfResults, account);
			results.addAll(bookshelfCards);
		}
		return results;
	}
	
	private Review reviewDeleteByIdUtility(Review review) throws Exception
	{
		Book book = bookService.findByApiId(review.getBookApiId());
		Double bookScore;
		int reviewsCount;
		if (review.getBookScore() != null) {
			bookScore = book.getScoreTotal() - review.getBookScore();
			book.setScoreTotal(bookScore);
			reviewsCount = book.getReviewCount() - 1;
			book.setReviewCount(reviewsCount);
			if (book.getReviewCount() == 0) {
				bookService.deleteByApiId(book.getApiId());
			} else {
				bookService.update(book);
			}
		}
		Review oldReview = reviewService.deleteById(review.getId());
		return oldReview;
	}

	// Função de utilidade para buscar por reviews sem capa, pelo Id do livro
	private List<Review> reviewFindByBookApiIdPaginateUtility(String bookApiId, long ownerId, int offset, int limit,
			boolean complete) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 1) {
			limit = 1;
		}
		List<Review> reviews;
		if (complete) {
			reviews = reviewService.findByBookApiIdAndPublicOrOwnerIdPaginate(bookApiId, ownerId, offset, limit);
		} else {
			reviews = reviewService.findByBookApiIdAndPublicPaginate(bookApiId, offset, limit);
		}
		Iterator<Review> reviewsIterator = reviews.listIterator();
		List<Review> reviewList;
		reviewList = new ArrayList<Review>();
		while (reviewsIterator.hasNext()) {
			Review review = reviewsIterator.next();
			reviewList.add(review);
		}
		return reviewList;
	}

	// Buscando pelas reviews de um usuário com capa
	private List<Map<String, Object>> reviewFindByOwnerIdPaginateUtility(long ownerId, int offset, int limit,
			boolean complete) throws Exception {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 1) {
			limit = 1;
		}
		List<Review> reviews;
		if (complete) {
			reviews = reviewService.findByOwnerIdPaginate(ownerId, offset, limit);
		} else {
			reviews = reviewService.findByOwnerIdAndPublicPaginate(ownerId, offset, limit);
		}
		List<Map<String, Object>> reviewsCards = new ArrayList<Map<String, Object>>();
		Iterator<Review> reviewsIterator = reviews.listIterator();
		while (reviewsIterator.hasNext()) {
			Review review = reviewsIterator.next();
			Map<String, Object> reviewCard = new HashMap<>();
			Map<String, Object> bookInfo = googleBooksService.findByApiId(review.getBookApiId(), "incomplete");
			reviewCard.put("id", review.getId());
			reviewCard.put("bookApiId", review.getBookApiId());
			reviewCard.put("text", review.getText());
			reviewCard.put("bookScore", review.getBookScore());
			reviewCard.put("creationDate", review.getCreationDate());
			reviewCard.put("editionDate", review.getEditionDate());
			reviewCard.put("owner", review.getOwner());
			reviewCard.put("privacy", review.isPrivacy());
			reviewCard.put("title", bookInfo.get("title"));
			reviewCard.put("cover", bookInfo.get("cover"));
			reviewsCards.add(reviewCard);
		}
		return reviewsCards;
	}
}