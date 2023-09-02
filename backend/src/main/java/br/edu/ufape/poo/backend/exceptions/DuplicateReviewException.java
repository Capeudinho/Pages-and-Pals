package br.edu.ufape.poo.backend.exceptions;

public class DuplicateReviewException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateReviewException() {
		super("duplicate review");
	}
}