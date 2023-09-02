package br.edu.ufape.poo.backend.exceptions;

public class InvalidNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidNameException() {
		super("invalid name");
	}
}