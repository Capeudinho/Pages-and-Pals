package br.edu.ufape.poo.backend.exceptions;

public class IncorrectEmailException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public IncorrectEmailException()
	{
		super ("incorrect email");
	}
}