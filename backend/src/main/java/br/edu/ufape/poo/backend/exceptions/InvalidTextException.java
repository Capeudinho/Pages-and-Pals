package br.edu.ufape.poo.backend.exceptions;

public class InvalidTextException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTextException() {
		super("invalid text");
	}
}