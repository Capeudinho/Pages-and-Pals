package br.edu.ufape.poo.backend.communication;

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
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.business.facade.Facade;
import br.edu.ufape.poo.backend.exceptions.AccessDeniedException;
import br.edu.ufape.poo.backend.exceptions.AuthenticationFailedException;
import br.edu.ufape.poo.backend.exceptions.IncorrectEmailException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectPasswordException;
import br.edu.ufape.poo.backend.exceptions.InvalidEmailException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameAccountException;
import br.edu.ufape.poo.backend.exceptions.InvalidPasswordException;
import br.edu.ufape.poo.backend.exceptions.TakenEmailException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/account")
public class AccountController
{
	@Autowired
	private Facade facade;
	
	@PostMapping("signup")
	public ResponseEntity<?> signUp(@RequestBody Account account) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Account newAccount = facade.accountSignUp(account);
			responseEntity = new ResponseEntity<Object>(newAccount, HttpStatus.CREATED);
		}
		catch (InvalidNameAccountException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid name");
		}
		catch (InvalidEmailException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid email");
		}
		catch (InvalidPasswordException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid password");
		}
		catch ( TakenEmailException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("email taken");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@PostMapping("login/{email}/{password}")
	public ResponseEntity<?> logIn(@PathVariable String email, @PathVariable String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Account account = facade.accountLogIn(email, password);
			responseEntity = new ResponseEntity<Object>(account, HttpStatus.OK);
		}
		catch (IncorrectEmailException | IncorrectPasswordException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect information");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
	
	@PatchMapping("update")
	public ResponseEntity<?> update(@RequestBody Account account, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Account newAccount = facade.accountUpdate(account, email, password);
			responseEntity = new ResponseEntity<Object>(newAccount, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (TakenEmailException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("taken email");
		}
		catch (InvalidNameAccountException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid name");
		}
		catch (InvalidEmailException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid email");
		}
		catch (InvalidPasswordException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("invalid password");
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
			Account account = facade.accountDeleteById(id, email, password);
			responseEntity = new ResponseEntity<Object>(account, HttpStatus.OK);
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
	public ResponseEntity<?> finOwndProfileById(@PathVariable Long id, @RequestHeader("email") String email, @RequestHeader("password") String password) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Map<String, Object> accountProfile = facade.accountFindOwnProfileById(id, email, password);
			responseEntity = new ResponseEntity<Object>(accountProfile, HttpStatus.OK);
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
	public ResponseEntity<?> findProfileById(@PathVariable Long id) throws Exception
	{
		ResponseEntity<Object> responseEntity;
		try
		{
			Map<String, Object> accountProfile = facade.accountFindProfileById(id);
			responseEntity = new ResponseEntity<Object>(accountProfile, HttpStatus.OK);
		}
		catch (IncorrectIdException exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("incorrect id");
		}
		catch (Exception exception)
		{
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
		return responseEntity;
	}
}