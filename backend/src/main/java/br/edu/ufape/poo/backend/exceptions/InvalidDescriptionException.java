package br.edu.ufape.poo.backend.exceptions;

public class InvalidDescriptionException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public InvalidDescriptionException()
	{
		super ("invalid description");
	}
}