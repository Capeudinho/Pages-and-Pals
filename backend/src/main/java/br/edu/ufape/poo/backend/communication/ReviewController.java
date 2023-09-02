package br.edu.ufape.poo.backend.communication;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.poo.backend.business.entity.Review;
import br.edu.ufape.poo.backend.business.facade.Facade;
import br.edu.ufape.poo.backend.exceptions.AccessDeniedException;
import br.edu.ufape.poo.backend.exceptions.AuthenticationFailedException;
import br.edu.ufape.poo.backend.exceptions.BookNotFoundException;
import br.edu.ufape.poo.backend.exceptions.DuplicateReviewException;
import br.edu.ufape.poo.backend.exceptions.IncorrectBookIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidBookScoreException;
import br.edu.ufape.poo.backend.exceptions.InvalidReviewCountException;
import br.edu.ufape.poo.backend.exceptions.InvalidReviewException;
import br.edu.ufape.poo.backend.exceptions.InvalidTextException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/review")
public class ReviewController {
	@Autowired
	private Facade facade;

	@PostMapping("create")
	public ResponseEntity<?> create(@RequestBody Review review, @RequestHeader("email") String email,
			@RequestHeader("password") String password) {
		ResponseEntity<Object> responseEntity;
		try {
			Review newReview = facade.reviewCreate(review, email, password);
			responseEntity = new ResponseEntity<Object>(newReview, HttpStatus.CREATED);
		} catch (InvalidReviewException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid review");
		} catch (DuplicateReviewException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("duplicate review");
		} catch (InvalidBookScoreException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid book score");
		} catch (InvalidTextException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid text");
		} catch (BookNotFoundException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("book not found");
		} catch (InvalidReviewCountException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.CONFLICT).body("invalid review count");
		} catch (AuthenticationFailedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}

	@PatchMapping("update")
	public ResponseEntity<?> update(@RequestBody Review review, @RequestHeader("email") String email,
			@RequestHeader("password") String password) {
		ResponseEntity<Object> responseEntity;
		try {
			Review newReview = facade.reviewUpdate(review, email, password);
			responseEntity = new ResponseEntity<Object>(newReview, HttpStatus.OK);
		} catch (IncorrectIdException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		} catch (InvalidReviewException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid review");
		} catch (InvalidBookScoreException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid book score");
		} catch (InvalidTextException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid text");
		} catch (InvalidReviewCountException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.CONFLICT).body("invalid review count");
		} catch (AuthenticationFailedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		} catch (AccessDeniedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}

	@DeleteMapping("deletebyid/{id}")
	public ResponseEntity<?> deleteById(@PathVariable long id, @RequestHeader("email") String email,
			@RequestHeader("password") String password) throws Exception {
		ResponseEntity<Object> responseEntity;
		try {
			Review oldReview = facade.reviewDeleteById(id, email, password);
			responseEntity = new ResponseEntity<Object>(oldReview, HttpStatus.OK);
		} catch (IncorrectIdException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		} catch (IncorrectBookIdException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect book id");
		} catch (DuplicateReviewException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("duplicate review");
		} catch (InvalidReviewCountException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.CONFLICT).body("invalid review count");
		} catch (AuthenticationFailedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		} catch (AccessDeniedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}

	@GetMapping("findbyowneridpaginate/{ownerId}")
	public ResponseEntity<?> findByOwnerIdPaginate(@PathVariable long ownerId,
			@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "0") int limit)
			throws Exception {
		ResponseEntity<Object> responseEntity;
		try {
			List<Map<String, Object>> reviewList = facade.reviewFindByOwnerIdPaginate(ownerId, offset, limit);
			responseEntity = new ResponseEntity<Object>(reviewList, HttpStatus.OK);
		} catch (IncorrectIdException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		} catch (AccessDeniedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("acess denied");
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}

	@GetMapping("findownbyowneridpaginate/{ownerId}")
	public ResponseEntity<?> findOwnByOwnerIdPaginate(@PathVariable long ownerId,
			@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "0") int limit,
			@RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception {
		ResponseEntity<Object> responseEntity;
		try {
			List<Map<String, Object>> reviewList = facade.reviewFindOwnByOwnerIdPaginate(ownerId, offset, limit, email,
					password);
			responseEntity = new ResponseEntity<Object>(reviewList, HttpStatus.OK);
		} catch (IncorrectIdException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		} catch (AuthenticationFailedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		} catch (AccessDeniedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("acess denied");
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}

	@GetMapping("findbybookapiidpaginate/{bookApiId}")
	public ResponseEntity<?> reviewFindByBookApiIdPaginate(@PathVariable String bookApiId,
			@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "0") int limit)
			throws Exception {
		ResponseEntity<Object> responseEntity;
		try {
			List<Review> reviewList = facade.reviewFindByBookApiIdPaginate(bookApiId, offset, limit);
			responseEntity = new ResponseEntity<Object>(reviewList, HttpStatus.OK);
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}

	@GetMapping("findbybookapiispaginateautenticaded/{bookApiId}")
	public ResponseEntity<?> reviewFindByBookApiIdPaginateAutenticaded(@PathVariable String bookApiId,
			@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "0") int limit,
			@RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception {
		ResponseEntity<Object> responseEntity;
		try {
			List<Review> reviewList = facade.reviewFindByBookApiIdPaginateAutenticaded(bookApiId, offset, limit, email,
					password);
			responseEntity = new ResponseEntity<Object>(reviewList, HttpStatus.OK);
		} catch (AuthenticationFailedException exception) {
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		} catch (Exception exception) {
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
}