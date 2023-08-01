package br.edu.ufape.poo.backend.exceptions;

import br.edu.ufape.poo.backend.business.basic.Account;

public class TakenEmailException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Account otherAccount;
	
	public TakenEmailException(Account otherAccount)
	{
		super ("email taken");
		this.otherAccount = otherAccount;
	}
	
	public Account getOtherAccount()
	{
		return this.otherAccount;
	}
}