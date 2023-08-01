package br.edu.ufape.poo.backend.exceptions;

import br.edu.ufape.poo.backend.business.basic.Account;

public class InvalidPasswordException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Account account;
	
	public InvalidPasswordException(Account account)
	{
		super ("invalid password");
		this.account = account;
	}
	
	public Account getAccount()
	{
		return this.account;
	}
}