package br.edu.ufape.poo.backend.business.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.data.AccountRepository;

@Service
@Transactional
public class AccountRegister
{
	@Autowired
	private AccountRepository accountRepository;
	
	public Account createAccount(Account account)
	{
		Account newAccount = accountRepository.save(account);
		return newAccount;
	}
}