package br.edu.ufape.poo.backend.business.service;

import br.edu.ufape.poo.backend.business.entity.Account;

public interface AccountServiceInterface {
	public Account signUp(Account account) throws Exception;
	public Account logIn(String email, String password) throws Exception;
	public Account update(Account account) throws Exception;
	public Account deleteById(Long id) throws Exception;
	public Account findById(Long id) throws Exception;
	public Account authenticate(String email, String password) throws Exception;
}