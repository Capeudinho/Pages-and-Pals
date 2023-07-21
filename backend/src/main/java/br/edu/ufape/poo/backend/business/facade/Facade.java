package br.edu.ufape.poo.backend.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.business.register.AccountRegister;

@Service
public class Facade
{
	@Autowired
	private AccountRegister accountRegister;
	
	public Account createAccount(Account account)
	{
		Account newAccount = accountRegister.createAccount(account);
		return newAccount;
	}
}