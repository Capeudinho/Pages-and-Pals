package br.edu.ufape.poo.backend.exceptions;

public class IncorrectIdException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public IncorrectIdException()
	{
		super ("incorrect id");
	}
}