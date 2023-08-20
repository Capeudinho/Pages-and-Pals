package br.edu.ufape.poo.backend.business.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.data.AccountRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class AccountServiceTest
{
	@Autowired
	private AccountServiceInterface accountService;
	@Autowired
	private AccountRepository accountRepository;
	
	@Test
	void signUpValid() throws Exception
	{
		Account account = new Account();
		account.setName("a");
		account.setEmail("a@a.a");
		account.setPassword("a");
		accountService.signUp(account);
	}
	
	@Test
	void logInValid() throws Exception
	{
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		accountRepository.save(account);
		accountService.logIn("a@a.a", "a");
	}
	
	@Test
	void updateValid() throws Exception
	{
		Account account = new Account();
		account.setName("a");
		account.setEmail("a@a.a");
		account.setPassword("a");
		account = accountRepository.save(account);
		account.setName("b");
		accountService.update(account);
	}
	
	@Test
	void deleteByIdValid() throws Exception
	{
		Account account = new Account();
		account = accountRepository.save(account);
		accountService.deleteById(account.getId());
	}
	
	@Test
	void findByIdValid() throws Exception
	{
		Account account = new Account();
		account = accountRepository.save(account);
		accountService.findById(account.getId());
	}
	
	@Test
	void authenticateValid() throws Exception
	{
		Account account = new Account();
		account.setEmail("a@a.a");
		account.setPassword("a");
		accountRepository.save(account);
		accountService.authenticate("a@a.a", "a");
	}
}