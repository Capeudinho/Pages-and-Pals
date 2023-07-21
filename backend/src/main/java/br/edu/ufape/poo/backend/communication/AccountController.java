package br.edu.ufape.poo.backend.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.business.facade.Facade;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("pagesandpals/api/v1/account")
public class AccountController
{
	@Autowired
	private Facade facade;
	
	@PostMapping("create")
	public ResponseEntity<Account> create(@RequestBody String accountJSON)
	{
		Account account = new Gson().fromJson(accountJSON, Account.class);
		Account newAccount = facade.createAccount(account);
		ResponseEntity<Account> newResponseEntity = new ResponseEntity<Account>(newAccount, HttpStatus.CREATED);
		return newResponseEntity;
	}
}