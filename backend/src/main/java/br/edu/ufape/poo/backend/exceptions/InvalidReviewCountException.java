package br.edu.ufape.poo.backend.exceptions;

public class InvalidReviewCountException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidReviewCountException() {
		super("invalid review count");
	}
}