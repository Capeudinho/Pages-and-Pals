package br.edu.ufape.poo.backend.exceptions;

public class InvalidTextReviewException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidTextReviewException () {
		super ("invalid text");
	}

}
