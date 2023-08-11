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

	@GetMapping("findBooksbyApiId/{apiId}")
	public ResponseEntity<Object> findBooksbyApiId(@PathVariable String apiId) {
		try {
			return new ResponseEntity<Object>(facade.findBooksbyApiId(apiId), HttpStatus.OK);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
		}
	}

	@GetMapping("findScoreByApiId/{apiId}")
	public ResponseEntity<Object> findScoreByApiId(@PathVariable String apiId) {
		try {
			return new ResponseEntity<Object>(facade.findScoreByApiId(apiId), HttpStatus.OK);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
		}
	}

	@GetMapping("findBookByAuthor/{author}/{maxResults}/{startIndex}")
	public ResponseEntity<?> findBookByAuthor(@PathVariable String author, @PathVariable int maxResults,
			@PathVariable int startIndex) {
		ResponseEntity<?> responseEntity;
		try {
			List<Object> booksList = facade.searchBookByAuthor(author, maxResults, startIndex);
			responseEntity = new ResponseEntity<List<Object>>(booksList, HttpStatus.OK);
			return responseEntity;
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);

		}

	}
}