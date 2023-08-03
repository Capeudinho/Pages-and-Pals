package br.edu.ufape.poo.backend.exceptions;

public class AccessDeniedException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public AccessDeniedException()
	{
		super ("access denied");
	}
}