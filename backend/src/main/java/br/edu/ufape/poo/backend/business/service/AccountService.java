package br.edu.ufape.poo.backend.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import br.edu.ufape.poo.backend.business.entity.Account;
import br.edu.ufape.poo.backend.data.AccountRepository;
import br.edu.ufape.poo.backend.exceptions.TakenEmailException;
import br.edu.ufape.poo.backend.exceptions.AuthenticationFailedException;
import br.edu.ufape.poo.backend.exceptions.IncorrectEmailException;
import br.edu.ufape.poo.backend.exceptions.IncorrectIdException;
import br.edu.ufape.poo.backend.exceptions.IncorrectPasswordException;
import br.edu.ufape.poo.backend.exceptions.InvalidEmailException;
import br.edu.ufape.poo.backend.exceptions.InvalidNameException;
import br.edu.ufape.poo.backend.exceptions.InvalidPasswordException;
import java.util.regex.Pattern;

@Service
@Transactional
public class AccountService implements AccountServiceInterface {
	@Autowired
	private AccountRepository accountRepository;

	public Account signUp(Account account) throws Exception {
		Account existingAccount = accountRepository.findByEmail(account.getEmail());
		if (account.getName() == null || account.getName().isBlank()) {
			throw new InvalidNameException();
		}
		if (account.getEmail() == null || account.getEmail().isBlank() || !Pattern
				.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(account.getEmail()).matches()) {
			throw new InvalidEmailException();
		}
		if (account.getPassword() == null || account.getPassword().isBlank()) {
			throw new InvalidPasswordException();
		}
		if (existingAccount != null) {
			throw new TakenEmailException();
		}
		account.setBiography("");
		account.setPrivacy(true);
		account.setPicture("");
		Account newAccount = accountRepository.save(account);
		return newAccount;
	}

	public Account logIn(String email, String password) throws Exception {
		Account account = accountRepository.findByEmail(email);
		if (account == null) {
			throw new IncorrectEmailException();
		} else if (!account.getPassword().equals(password)) {
			throw new IncorrectPasswordException();
		}
		return account;
	}

	public Account update(Account account) throws Exception {
		Account oldAccount = accountRepository.findById(account.getId()).orElse(null);
		Account existingAccount = accountRepository.findByEmail(account.getEmail());
		if (oldAccount == null) {
			throw new IncorrectIdException();
		}
		if (existingAccount != null && account.getId() != existingAccount.getId()) {
			throw new TakenEmailException();
		}
		if (account.getName() == null || account.getName().isBlank()) {
			throw new InvalidNameException();
		}
		if (account.getEmail() == null || account.getEmail().isBlank() || !Pattern
				.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(account.getEmail()).matches()) {
			throw new InvalidEmailException();
		}
		if (account.getPassword() == null || account.getPassword().isBlank()) {
			throw new InvalidPasswordException();
		}
		Account newAccount = accountRepository.save(account);
		return newAccount;
	}

	public Account deleteById(Long id) throws Exception {
		Account account = accountRepository.findById(id).orElse(null);
		if (account == null) {
			throw new IncorrectIdException();
		}
		accountRepository.delete(account);
		return account;
	}

	public Account findById(Long id) throws Exception {
		Account account = accountRepository.findById(id).orElse(null);
		if (account == null) {
			throw new IncorrectIdException();
		}
		return account;
	}

	public Account authenticate(String email, String password) throws Exception {
		Account account = accountRepository.findByEmailAndPassword(email, password);
		if (account == null) {
			throw new AuthenticationFailedException();
		}
		return account;
	}
}