package br.edu.ufape.poo.backend.exceptions;

public class InvalidBookScoreReviewException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidBookScoreReviewException () {
		super ("invalid book score");
	}

}