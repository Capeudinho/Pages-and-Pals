package br.edu.ufape.poo.backend.exceptions;

public class InvalidPasswordException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public InvalidPasswordException()
	{
		super ("invalid password");
	}
}