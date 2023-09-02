package br.edu.ufape.poo.backend.exceptions;

public class IncorrectBookIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncorrectBookIdException() {
		super("incorrect book id");
	}
}