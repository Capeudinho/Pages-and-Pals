package br.edu.ufape.poo.backend.exceptions;

public class InvalidBookScoreException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidBookScoreException() {
		super("invalid book score");
	}
}