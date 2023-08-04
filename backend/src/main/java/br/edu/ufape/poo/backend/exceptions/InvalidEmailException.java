package br.edu.ufape.poo.backend.exceptions;

import br.edu.ufape.poo.backend.business.entity.Account;

public class InvalidEmailException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Account account;
	
	public InvalidEmailException(Account account)
	{
		super ("invalid email");
		this.account = account;
	}
	
	public Account getAccount()
	{
		return this.account;
	}
}