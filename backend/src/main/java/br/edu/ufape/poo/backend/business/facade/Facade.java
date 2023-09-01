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

	public Account accountSignUp(Account account) throws Exception {
		Account newAccount = accountService.signUp(account);
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
		return newAccount;
	}

	public Account accountLogIn(String email, String password) throws Exception {
		Account account = accountService.logIn(email, password);
		return account;
	}

	public Account accountUpdate(Account account, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != account.getId()) {
			throw new AccessDeniedException();
		}
		Account newAccount = accountService.update(account);
		return newAccount;
	}

	public Account accountDeleteById(Long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != id) {
			throw new AccessDeniedException();
		}
		bookshelfService.deleteByOwnerId(id);
		// Delete other stuff
		accountService.deleteById(id);
		return requestingAccount;
	}

	public Map<String, Object> accountFindOwnById(Long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != id) {
			throw new AccessDeniedException();
		}
		Map<String, Object> accountProfile = accountFindByIdUtility(requestingAccount, true);
		return accountProfile;
	}

	public Map<String, Object> accountFindById(Long id) throws Exception {
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

	public Bookshelf bookshelfAddBookApiIdById(Long id, String apiId, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.addBookApiIdById(id, apiId);
		return newBookshelf;
	}

	public Bookshelf bookshelfRemoveBookApiIdById(Long id, String apiId, String email, String password)
			throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf newBookshelf = bookshelfService.removeBookApiIdById(id, apiId);
		return newBookshelf;
	}

	public Bookshelf bookshelfDeleteById(Long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Bookshelf oldBookshelf = bookshelfService.deleteById(id);
		return oldBookshelf;
	}

	public Map<String, Object> bookshelfFindOwnById(Long id, String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		Map<String, Object> bookshelfCard = bookshelfFindByIdUtility(bookshelf, true);
		return bookshelfCard;
	}

	public Map<String, Object> bookshelfFindById(Long id) throws Exception {
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (!bookshelf.getOwner().isPrivacy()) {
			throw new AccessDeniedException();
		}
		Map<String, Object> bookshelfCard = bookshelfFindByIdUtility(bookshelf, false);
		return bookshelfCard;
	}

	public List<Map<String, Object>> bookshelfFindOwnByOwnerIdPaginate(Long ownerId, int offset, int limit,
			String email, String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != ownerId) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfCards = bookshelfFindByOwnerIdPaginateUtility(requestingAccount, offset,
				limit, true);
		return bookshelfCards;
	}

	public List<Map<String, Object>> bookshelfFindByOwnerIdPaginate(Long ownerId, int offset, int limit)
			throws Exception {
		Account account = accountService.findById(ownerId);
		if (!account.isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfCards = bookshelfFindByOwnerIdPaginateUtility(account, offset, limit, false);
		return bookshelfCards;
	}

	public List<Map<String, Object>> bookshelfFindOwnBooksByIdPaginate(Long id, int offset, int limit, String email,
			String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (requestingAccount.getId() != bookshelf.getOwner().getId()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> books = bookshelfFindBooksByIdPaginateUtility(bookshelf, offset, limit);
		return books;
	}

	public List<Map<String, Object>> bookshelfFindBooksByIdPaginate(Long id, int offset, int limit) throws Exception {
		Bookshelf bookshelf = bookshelfService.findById(id);
		if (!bookshelf.isPrivacy() || !bookshelf.getOwner().isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> books = bookshelfFindBooksByIdPaginateUtility(bookshelf, offset, limit);
		return books;
	}

	public List<Map<String, Object>> bookshelfFindOwnSelectByOwnerId(Long ownerId, String apiId, String email,
			String password) throws Exception {
		Account requestingAccount = accountService.authenticate(email, password);
		List<Map<String, Object>> bookshelfSelects = bookshelfFindSelectByOwnerIdUtility(requestingAccount, apiId,
				true);
		return bookshelfSelects;
	}

	public List<Map<String, Object>> bookshelfFindSelectByOwnerId(Long ownerId, String apiId) throws Exception {
		Account account = accountService.findById(ownerId);
		if (!account.isPrivacy()) {
			throw new AccessDeniedException();
		}
		List<Map<String, Object>> bookshelfSelects = bookshelfFindSelectByOwnerIdUtility(account, apiId, false);
		return bookshelfSelects;
	}

	// BOOK

	public Map<String, Object> findBookByApiId(String apiId, String extractInfo) throws Exception {
		return googleBooksService.findByApiId(apiId, extractInfo);

	}

	public List<Object> advancedSearch(String term, String title, String author, String subject, String publisher, String isbn, Integer maxResults, Integer startIndex, String ownerName, String bookshelfName,String resultType) {
		List<Object> results = new ArrayList<>();
		List<Object> bookResults = new ArrayList<>();
		List<Object> bookshelfResults = new ArrayList<>();

		if ("all".equals(resultType) || "book".equals(resultType)) {
			bookResults = googleBooksService.advancedSearchResults(term, title, author, subject, publisher, isbn, maxResults, startIndex, "incomplete");
			results.addAll(bookResults);

		}

		if ("all".equals(resultType) || "bookshelf".equals(resultType)) {
			bookshelfResults = bookshelfService.findByOwnerAndBookshelfName(ownerName, bookshelfName, startIndex, maxResults);
			results.addAll(bookshelfResults);
		}

		return results;
	}

	// REVIEW

	// Verificando se a conta criadora da Review existe
	public Review reviewCreate(Review review, String email, String password) throws Exception {

		Account requestingAccount = accountService.authenticate(email, password);
		review.setOwner(requestingAccount);

		Book book = bookService.findByApiId(review.getBookApiId());

		if (book == null) {
			googleBooksService.findByApiId(review.getBookApiId(), "incomplete");
			book = new Book();
			book = bookService.create(book);
		}

		Review newReview = reviewService.create(review);
		Double bookScore = book.getScoreTotal() + review.getBookScore();
		book.setScoreTotal(bookScore);
		int reviewsCount = book.getReviewCount() + 1;
		book.setReviewCount(reviewsCount);
		book.setApiId(review.getBookApiId());
		bookService.update(book);

		return newReview;
	}

	// Verificando se a conta que quer editar existe e se é criadora da Review
	public Review reviewUpdate(Review review, String email, String password) throws Exception {

		Account requestingAccount = accountService.authenticate(email, password);
		if (requestingAccount.getId() != review.getOwner().getId()) {
			throw new AccessDeniedException();
		}

		Review oldReview = reviewService.findById(review.getId());
		Book book = bookService.findByApiId(review.getBookApiId());

		Double bookScore = book.getScoreTotal() - oldReview.getBookScore();
		bookScore = bookScore + review.getBookScore();
		book.setScoreTotal(bookScore);
		bookService.update(book);

		Review newReview = reviewService.update(review);

		return newReview;
	}

	// Verificando se a conta que quer deletar existe e se é criadora da Review
	public Review reviewDeleteById(Long id, String email, String password) throws Exception {

		// Testar se quando lança a exeção desfaz a exclusão da review no controller

		Account requestingAccount = accountService.authenticate(email, password);
		Review oldReview = reviewService.deleteById(id);

		if (requestingAccount.getId() != oldReview.getOwner().getId()) {
			throw new AccessDeniedException();
		}

		Book book = bookService.findByApiId(oldReview.getBookApiId());
		Double bookScore = book.getScoreTotal() - oldReview.getBookScore();
		book.setScoreTotal(bookScore);

		int reviewsCount = book.getReviewCount() - 1;
		book.setReviewCount(reviewsCount);

		if (book.getReviewCount() == 0) {
			bookService.deleteByApiId(book.getApiId());
		} else {
			bookService.update(book);
		}

		return oldReview;
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

	// Utility

	public Map<String, Object> accountFindByIdUtility(Account account, boolean complete) throws Exception {
		Map<String, Object> accountProfile = new HashMap<>();
		accountProfile.put("id", account.getId());
		accountProfile.put("name", account.getName());
		accountProfile.put("picture", account.getPicture());
		accountProfile.put("privacy", account.isPrivacy());
		if (account.isPrivacy() || complete) {
			int bookshelfCount = bookshelfService.countByOwnerId(account.getId());
			accountProfile.put("email", account.getEmail());
			accountProfile.put("biography", account.getBiography());
			accountProfile.put("bookshelfCount", bookshelfCount);
		}
		return accountProfile;
	}

	public Map<String, Object> bookshelfFindByIdUtility(Bookshelf bookshelf, boolean complete) throws Exception {
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
		int bookshelfCount = bookshelfService.countByOwnerId(account.getId());
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 0) {
			limit = 0;
		}
		if (offset > bookshelfCount) {
			offset = bookshelfCount;
		}
		if (limit > bookshelfCount - offset) {
			limit = bookshelfCount - offset;
		}
		List<Bookshelf> bookshelves = bookshelfService.findByOwnerIdPaginate(account.getId(), offset, limit);
		List<Map<String, Object>> bookshelfCards = new ArrayList<Map<String, Object>>();
		for (int bookshelfIndex = 0; bookshelfIndex < bookshelves.size(); bookshelfIndex++) {
			Bookshelf bookshelf = bookshelves.get(bookshelfIndex);
			Map<String, Object> bookshelfCard = new HashMap<>();
			bookshelfCard.put("id", bookshelf.getId());
			bookshelfCard.put("name", bookshelf.getName());
			bookshelfCard.put("privacy", bookshelf.isPrivacy());
			bookshelfCard.put("owner", bookshelf.getOwner());
			if (bookshelf.isPrivacy() || complete) {
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

	private List<Map<String, Object>> bookshelfFindBooksByIdPaginateUtility(Bookshelf bookshelf, int offset, int limit)
			throws Exception {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 0) {
			limit = 0;
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

	// Função de utilidade para buscar por reviews sem capa, pelo Id do livro
	private List<Review> reviewFindByBookApiIdPaginateUtility(String bookApiId, long ownerId, int offset, int limit,
			boolean complete) {
		int reviewCount = reviewService.countByBookApiId(bookApiId);

		if (offset < 0) {
			offset = 0;
		}
		if (limit < 0) {
			limit = 0;
		}
		if (offset > reviewCount) {
			offset = reviewCount;
		}
		if (limit > reviewCount - offset) {
			limit = reviewCount - offset;
		}
		List<Review> reviews = reviewService.findByBookApiIdPaginate(bookApiId, offset, limit);
		Iterator<Review> reviewsIterator = reviews.listIterator();
		List<Review> reviewList;
		reviewList = new ArrayList<Review>();

		while (reviewsIterator.hasNext()) {

			Review review = reviewsIterator.next();

			if (!review.isPrivacy() || (complete && review.getOwner().getId() == ownerId)) {

				reviewList.add(review);
			}

		}
		return reviewList;
	}

	// Manipular as notas das Reviews

	// Buscando pelas reviews de um usuário com capa
	private List<Map<String, Object>> reviewFindByOwnerIdPaginateUtility(long ownerId, int offset, int limit,
			boolean complete) {
		int reviewCount = reviewService.countByOwnerId(ownerId);
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 0) {
			limit = 0;
		}
		if (offset > reviewCount) {
			offset = reviewCount;
		}
		if (limit > reviewCount - offset) {
			limit = reviewCount - offset;
		}
		List<Review> reviews = reviewService.findByOwnerIdPaginate(ownerId, offset, limit);
		List<Map<String, Object>> reviewsCards = new ArrayList<Map<String, Object>>();
		Iterator<Review> reviewsIterator = reviews.listIterator();

		while (reviewsIterator.hasNext()) {

			Review review = reviewsIterator.next();
			Map<String, Object> reviewCard = new HashMap<>();

			if (!review.isPrivacy() || complete) {
				reviewCard.put("id", review.getId());
				reviewCard.put("bookApiId", review.getBookApiId());
				reviewCard.put("text", review.getText());
				reviewCard.put("bookScore", review.getBookScore());
				reviewCard.put("creationDate", review.getCreationDate());
				reviewCard.put("editionDate", review.getEditionDate());
				reviewCard.put("bookScore", review.getOwner());
				reviewCard.put("privacy", review.isPrivacy());

				String cover = googleBooksService.findCoverByApiId(review.getBookApiId());
				reviewCard.put("cover", cover);

				reviewsCards.add(reviewCard);
			}

		}
		return reviewsCards;
	}
}