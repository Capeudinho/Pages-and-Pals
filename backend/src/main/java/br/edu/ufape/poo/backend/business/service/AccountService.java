package br.edu.ufape.poo.backend.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import br.edu.ufape.poo.backend.business.basic.Account;
import br.edu.ufape.poo.backend.data.AccountRepository;
import br.edu.ufape.poo.backend.exceptions.TakenEmailException;
import br.edu.ufape.poo.backend.exceptions.AuthenticationFailedException;
import br.edu.ufape.poo.backend.exceptions.IncorrectEmailException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectPasswordException;
import br.edu.ufape.poo.backend.exceptions.InvalidEmailException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameAccountException;
import br.edu.ufape.poo.backend.exceptions.InvalidPasswordException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class AccountService
{
	@Autowired
	private AccountRepository accountRepository;
	
	public Account signUp(Account account) throws Exception
	{
		Optional<Account> accountOptional = accountRepository.findByEmail(account.getEmail());
		if (account.getName().isBlank())
		{
			throw new InvalidNameAccountException(account);
		}
		if (account.getEmail().isBlank() || !Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(account.getEmail()).matches())
		{
			throw new InvalidEmailException(account);
		}
		if (account.getPassword().isBlank())
		{
			throw new InvalidPasswordException(account);
		}
		if (accountOptional.isPresent())
		{
			throw new TakenEmailException(accountOptional.get());
		}
		account.setBiography("");
		account.setPrivacy(true);
		account.setPicture("");
		Account newAccount = accountRepository.save(account);
		return newAccount;
	}
	
	public Account logIn(String email, String password) throws Exception
	{
		Optional<Account> accountOptional = accountRepository.findByEmail(email);
		if (!accountOptional.isPresent())
		{
			throw new IncorrectEmailException();
		}
		else if (!accountOptional.get().getPassword().equals(password))
		{
			throw new IncorrectPasswordException();
		}
		Account account = accountOptional.get();
		return account;
	}

	public Account update(Account account) throws Exception
	{
		Optional<Account> accountOptional = accountRepository.findById(account.getId());
		Optional<Account> otherAccountOptional = accountRepository.findByEmail(account.getEmail());
		if (!accountOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		if (otherAccountOptional.isPresent() && account.getId() != otherAccountOptional.get().getId())
		{
			throw new TakenEmailException(otherAccountOptional.get());
		}
		if (account.getName().isBlank())
		{
			throw new InvalidNameAccountException(account);
		}
		if (account.getEmail().isBlank() || !Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(account.getEmail()).matches())
		{
			throw new InvalidEmailException(account);
		}
		if (account.getPassword().isBlank())
		{
			throw new InvalidPasswordException(account);
		}
		Account newAccount = accountRepository.save(account);
		return newAccount;
	}
	
	public Account deleteById(Long id) throws Exception
	{
		Optional<Account> accountOptional = accountRepository.findById(id);
		if (!accountOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		accountRepository.delete(accountOptional.get());
		return accountOptional.get();
	}
	
	public Account findById(Long id) throws Exception
	{
		Optional<Account> accountOptional = accountRepository.findById(id);
		if (!accountOptional.isPresent())
		{
			throw new IncorrectIdException();
		}
		Account account = accountOptional.get();
		return account;
	}
	
	public Account authenticate(String email, String password) throws Exception
	{
		Optional<Account> accountOptional = accountRepository.findByEmailAndPassword(email, password);
		if (!accountOptional.isPresent())
		{
			throw new AuthenticationFailedException();
		}
		Account account = accountOptional.get();
		return account;
	}
}