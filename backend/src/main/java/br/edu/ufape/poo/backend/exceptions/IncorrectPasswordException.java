package br.edu.ufape.poo.backend.exceptions;

public class IncorrectPasswordException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException() {
		super("incorrect password");
	}
}