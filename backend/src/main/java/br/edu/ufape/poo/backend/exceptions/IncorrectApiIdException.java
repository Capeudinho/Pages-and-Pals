package br.edu.ufape.poo.backend.exceptions;

public class IncorrectApiIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncorrectApiIdException() {
		super("incorrect api id");
	}
}