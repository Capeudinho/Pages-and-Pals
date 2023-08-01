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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.poo.backend.business.basic.Bookshelf;
import br.edu.ufape.poo.backend.business.facade.Facade;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameBookshelfException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/bookshelf")
public class BookshelfController
{
	@Autowired
	private Facade facade;
	
	@PostMapping("create")
	public ResponseEntity<?> create(@RequestBody Bookshelf bookshelf) throws Exception
	{
		try
		{
			Bookshelf newBookshelf = facade.bookshelfCreate(bookshelf);
			ResponseEntity<Bookshelf> responseEntity = new ResponseEntity<Bookshelf>(newBookshelf, HttpStatus.CREATED);
			return responseEntity;
		}
		catch (InvalidNameBookshelfException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid name");
			return responseEntity;
		}
	}
	
	@PatchMapping("update")
	public ResponseEntity<?> update(@RequestBody Bookshelf bookshelf) throws Exception
	{
		try
		{
			Bookshelf newBookshelf = facade.bookshelfUpdate(bookshelf);
			ResponseEntity<Bookshelf> responseEntity = new ResponseEntity<Bookshelf>(newBookshelf, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
		catch (InvalidNameBookshelfException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid name");
			return responseEntity;
		}
	}
	
	@GetMapping("findcardsbyownerid/{id}")
	public ResponseEntity<?> bookshelfFindCardsByOwnerId(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) throws Exception
	{
		try
		{
			List<Map<String, Object>> bookshelfCards = facade.bookshelfFindCardsByOwnerId(id, page, size);
			ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(bookshelfCards, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
	}
	
	@GetMapping("findbyid/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) throws Exception
	{
		try
		{
			Bookshelf bookshelf = facade.bookshelfFindById(id);
			ResponseEntity<Bookshelf> responseEntity = new ResponseEntity<Bookshelf>(bookshelf, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
	}
	
	@GetMapping("findbookcardsbyid/{id}")
	public ResponseEntity<?> findBookCardsById(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) throws Exception
	{
		try
		{
			List<Map<String, Object>> books = facade.bookshelfFindBookCardsById(id, page, size);
			ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(books, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
	}
	
	@DeleteMapping("deletebyid/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) throws Exception
	{
		try
		{
			Bookshelf bookshelf = facade.bookshelfDeleteById(id);
			ResponseEntity<Bookshelf> responseEntity = new ResponseEntity<Bookshelf>(bookshelf, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
	}
}