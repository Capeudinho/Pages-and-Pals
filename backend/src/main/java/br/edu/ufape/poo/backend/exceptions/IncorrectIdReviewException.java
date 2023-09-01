package br.edu.ufape.poo.backend.exceptions;

public class IncorrectIdReviewException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public IncorrectIdReviewException () {
		super ("incorrect review id");
	}

}
