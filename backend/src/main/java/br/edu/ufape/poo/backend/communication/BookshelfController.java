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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import br.edu.ufape.poo.backend.business.facade.Facade;
import br.edu.ufape.poo.backend.exceptions.AccessDeniedException;
import br.edu.ufape.poo.backend.exceptions.AuthenticationFailedException;
import br.edu.ufape.poo.backend.exceptions.DuplicateApiIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectApiIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/bookshelf")
public class BookshelfController
{
	@Autowired
	private Facade facade;
	
	@PostMapping("create")
	public ResponseEntity<?> create(@RequestBody Bookshelf bookshelf, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Bookshelf newBookshelf = facade.bookshelfCreate(bookshelf, email, password);
			responseEntity = new ResponseEntity<Object>(newBookshelf, HttpStatus.CREATED);
		}
		catch (InvalidNameException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid name");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@PatchMapping("update")
	public ResponseEntity<?> update(@RequestBody Bookshelf bookshelf, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Bookshelf newBookshelf = facade.bookshelfUpdate(bookshelf, email, password);
			responseEntity = new ResponseEntity<Object>(newBookshelf, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (InvalidNameException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid name");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@PatchMapping("addbookapiidbyid/{id}")
	public ResponseEntity<?> addBookApiIdById(@PathVariable Long id, @RequestBody String json, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
			if (jsonObject != null && jsonObject.get("apiId") != null)
			{
				String apiId = jsonObject.get("apiId").getAsString();
				Bookshelf newBookshelf = facade.bookshelfAddBookApiIdById(id, apiId, email, password);
				responseEntity = new ResponseEntity<Object>(newBookshelf, HttpStatus.OK);
			}
			else
			{
				responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid body");
			}
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (DuplicateApiIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("duplicate api id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@PatchMapping("removebookapiidbyid/{id}")
	public ResponseEntity<?> removeBookApiIdById(@PathVariable Long id, @RequestBody String json, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
			String apiId = jsonObject.get("apiId").getAsString();
			Bookshelf newBookshelf = facade.bookshelfRemoveBookApiIdById(id, apiId, email, password);
			responseEntity = new ResponseEntity<Object>(newBookshelf, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (IncorrectApiIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect api id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@DeleteMapping("deletebyid/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Bookshelf bookshelf = facade.bookshelfDeleteById(id, email, password);
			responseEntity = new ResponseEntity<Object>(bookshelf, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findownbyid/{id}")
	public ResponseEntity<?> bookshelfFindOwnById(@PathVariable Long id, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Map<String, Object> bookshelfCard = facade.bookshelfFindOwnById(id, email, password);
			responseEntity = new ResponseEntity<Object>(bookshelfCard, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findbyid/{id}")
	public ResponseEntity<?> bookshelfFindById(@PathVariable Long id) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Map<String, Object> bookshelfCard = facade.bookshelfFindById(id);
			responseEntity = new ResponseEntity<Object>(bookshelfCard, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findownbyowneridpaginate/{ownerId}")
	public ResponseEntity<?> bookshelfFindOwnByOwnerIdPaginate(@PathVariable Long ownerId, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "1") int limit, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			List<Map<String, Object>> bookshelfCards = facade.bookshelfFindOwnByOwnerIdPaginate(ownerId, offset, limit, email, password);
			responseEntity = new ResponseEntity<Object>(bookshelfCards, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findbyowneridpaginate/{ownerId}")
	public ResponseEntity<?> bookshelfFindByOwnerIdPaginate(@PathVariable Long ownerId, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "1") int limit) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			List<Map<String, Object>> bookshelfCards = facade.bookshelfFindByOwnerIdPaginate(ownerId, offset, limit);
			responseEntity = new ResponseEntity<Object>(bookshelfCards, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findownbooksbyidpaginate/{id}")
	public ResponseEntity<?> findOwnBooksByIdPaginate(@PathVariable Long id, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "1") int limit, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			List<Map<String, Object>> books = facade.bookshelfFindOwnBooksByIdPaginate(id, offset, limit, email, password);
			responseEntity = new ResponseEntity<Object>(books, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findbooksbyidpaginate/{id}")
	public ResponseEntity<?> findBooksByIdPaginate(@PathVariable Long id, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "1") int limit) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			List<Map<String, Object>> books = facade.bookshelfFindBooksByIdPaginate(id, offset, limit);
			responseEntity = new ResponseEntity<Object>(books, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findownselectbyowneridandapiid/{ownerId}/{apiId}")
	public ResponseEntity<?> bookshelfFindOwnSelectByOwnerId(@PathVariable Long ownerId, @PathVariable String apiId, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			List<Map<String, Object>> bookshelfSelects = facade.bookshelfFindOwnSelectByOwnerId(ownerId, apiId, email, password);
			responseEntity = new ResponseEntity<Object>(bookshelfSelects, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AuthenticationFailedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("authentication failed");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@GetMapping("findselectbyowneridandapiid/{ownerId}/{apiId}")
	public ResponseEntity<?> bookshelfFindSelectByOwnerId(@PathVariable Long ownerId, @PathVariable String apiId) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			List<Map<String, Object>> bookshelfSelects = facade.bookshelfFindSelectByOwnerId(ownerId, apiId);
			responseEntity = new ResponseEntity<Object>(bookshelfSelects, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (AccessDeniedException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
}