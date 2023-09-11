package br.edu.ufape.poo.backend.communication;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.poo.backend.business.facade.Facade;
import br.edu.ufape.poo.backend.exceptions.AuthenticationFailedException;
import br.edu.ufape.poo.backend.exceptions.BookNotFoundException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/book")
public class BookController {
	@Autowired
	private Facade facade;

	@GetMapping("findbyapiid/{apiId}")
	public ResponseEntity<Object> findByApiId(@PathVariable String apiId) {
		try {
			return new ResponseEntity<Object>(facade.bookFindByApiId(apiId,"complete"), HttpStatus.OK);
		} catch (BookNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("book not found");
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
	}

	@GetMapping("/findbyadvancedauthenticated")
	public ResponseEntity<?> findOwnByAdvanced(
			@RequestParam(required = false, name = "term") String term,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "subject") String subject,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(defaultValue = "0", name = "offset") int offset,
			@RequestParam(defaultValue = "1", name = "limit") int limit,
			@RequestParam(required = false, name = "ownername") String ownerName,
			@RequestParam(required = false, name = "bookshelfname") String bookshelfName,
			@RequestParam(required = false, name = "resulttype") String resultType,
			@RequestHeader("email") String email,
			@RequestHeader("password") String password) {
		ResponseEntity<?> responseEntity;
		try {
			List<Map<String, Object>> resultsList = facade.bookFindByAdvancedAuthenticated(term, title, author, subject, publisher, isbn, offset,
					limit, ownerName, bookshelfName, resultType, email, password);
			responseEntity = new ResponseEntity<List<Map<String, Object>>>(resultsList, HttpStatus.OK);
			return responseEntity;
		} catch (AuthenticationFailedException exception) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
	}
	
	@GetMapping("/findbyadvanced")
	public ResponseEntity<?> findByAdvanced(
			@RequestParam(required = false, name = "term") String term,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "subject") String subject,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(defaultValue = "0", name = "offset") int offset,
			@RequestParam(defaultValue = "1", name = "limit") int limit,
			@RequestParam(required = false, name = "ownername") String ownerName,
			@RequestParam(required = false, name = "bookshelfname") String bookshelfName,
			@RequestParam(required = false, name = "resulttype") String resultType) {
		ResponseEntity<?> responseEntity;
		try {
			List<Map<String, Object>> resultsList = facade.bookFindByAdvanced(term, title, author, subject, publisher, isbn, offset,
					limit, ownerName, bookshelfName, resultType);
			responseEntity = new ResponseEntity<List<Map<String, Object>>>(resultsList, HttpStatus.OK);
			return responseEntity;
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
		}
	}
}