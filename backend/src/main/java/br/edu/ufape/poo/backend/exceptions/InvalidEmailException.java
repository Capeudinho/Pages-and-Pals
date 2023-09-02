package br.edu.ufape.poo.backend.exceptions;

public class InvalidEmailException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidEmailException() {
		super("invalid email");
	}
}