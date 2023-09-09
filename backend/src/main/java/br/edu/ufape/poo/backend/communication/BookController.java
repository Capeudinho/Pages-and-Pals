package br.edu.ufape.poo.backend.communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.poo.backend.business.facade.Facade;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/book")
public class BookController {

	@Autowired
	private Facade facade;

	@GetMapping("findbookbyapiid/{apiId}")
	public ResponseEntity<Object> findBookByApiId(@PathVariable String apiId) {
		try {
			return new ResponseEntity<Object>(facade.findBookByApiId(apiId,"complete"), HttpStatus.OK);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
		}
	}

	@GetMapping("/advancedsearch")
	public ResponseEntity<?> findBooksAdvancedSearch(
			@RequestParam(required = false, name = "term") String term,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "author") String author,
			@RequestParam(required = false, name = "subject") String subject,
			@RequestParam(required = false, name = "publisher") String publisher,
			@RequestParam(required = false, name = "isbn") String isbn,
			@RequestParam(defaultValue = "1", name = "maxResults") Integer maxResults,
			@RequestParam(defaultValue = "0", name = "startIndex") Integer startIndex,
			@RequestParam(required = false, name = "ownerName") String ownerName,
			@RequestParam(required = false, name = "bookshelfName") String bookshelfName,
			@RequestParam(required = false, name = "resultType") String resultType
			) {
		ResponseEntity<?> responseEntity;
		try {
			List<Object> resultsList = facade.advancedSearch(term, title, author, subject, publisher, isbn, maxResults, startIndex, ownerName, bookshelfName, resultType);
			responseEntity = new ResponseEntity<List<Object>>(resultsList, HttpStatus.OK);
			return responseEntity;
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);

		}

	}

}