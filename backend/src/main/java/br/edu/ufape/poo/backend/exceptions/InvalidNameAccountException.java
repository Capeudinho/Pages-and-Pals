package br.edu.ufape.poo.backend.exceptions;

import br.edu.ufape.poo.backend.business.entity.Account;

public class InvalidNameAccountException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Account account;
	
	public InvalidNameAccountException(Account account)
	{
		super ("invalid name");
		this.account = account;
	}
	
	public Account getAccount()
	{
		return this.account;
	}
}