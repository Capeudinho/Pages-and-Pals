package br.edu.ufape.poo.backend.communication;

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
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.business.facade.Facade;
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
		try
		{
			Account newAccount = facade.accountSignUp(account);
			ResponseEntity<Account> responseEntity = new ResponseEntity<Account>(newAccount, HttpStatus.CREATED);
			return responseEntity;
		}
		catch (InvalidNameAccountException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid name");
			return responseEntity;
		}
		catch (InvalidEmailException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid email");
			return responseEntity;
		}
		catch (InvalidPasswordException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid password");
			return responseEntity;
		}
		catch ( TakenEmailException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("email taken");
			return responseEntity;
		}
	}
	
	@PostMapping("login/{email}/{password}")
	public ResponseEntity<?> logIn(@PathVariable String email, @PathVariable String password) throws Exception
	{
		try {
			Account account = facade.accountLogIn(email, password);
			ResponseEntity<Account> responseEntity = new ResponseEntity<Account>(account, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectEmailException | IncorrectPasswordException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect information");
			return responseEntity;
		}
	}
	
	@PatchMapping("update")
	public ResponseEntity<?> update(@RequestBody Account account) throws Exception
	{
		try
		{
			Account newAccount = facade.accountUpdate(account);
			ResponseEntity<Account> responseEntity = new ResponseEntity<Account>(newAccount, HttpStatus.OK);
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
		catch (TakenEmailException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("taken email");
			return responseEntity;
		}
		catch (InvalidNameAccountException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid name");
			return responseEntity;
		}
		catch (InvalidEmailException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid email");
			return responseEntity;
		}
		catch (InvalidPasswordException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("invalid password");
			return responseEntity;
		}
	}
	
	@GetMapping("findbyid/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) throws Exception
	{
		try
		{
			Account account = facade.accountFindById(id);
			ResponseEntity<Account> responseEntity = new ResponseEntity<Account>(account, HttpStatus.OK);
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
			Account account = facade.accountDeleteById(id);
			ResponseEntity<Account> responseEntity = new ResponseEntity<Account>(account, HttpStatus.OK);
			// Delete other stuff
			return responseEntity;
		}
		catch (IncorrectIdException exception)
		{
			ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("incorrect id");
			return responseEntity;
		}
	}
}