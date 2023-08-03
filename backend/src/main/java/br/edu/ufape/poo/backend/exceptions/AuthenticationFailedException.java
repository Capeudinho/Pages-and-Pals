package br.edu.ufape.poo.backend.exceptions;

public class AuthenticationFailedException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public AuthenticationFailedException()
	{
		super ("authentication failed");
	}
}