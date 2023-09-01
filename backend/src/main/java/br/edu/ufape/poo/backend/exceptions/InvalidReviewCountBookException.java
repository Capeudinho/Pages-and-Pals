package br.edu.ufape.poo.backend.exceptions;

public class InvalidReviewCountBookException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidReviewCountBookException()
	{
		super ("invalid review count");
	}
}
