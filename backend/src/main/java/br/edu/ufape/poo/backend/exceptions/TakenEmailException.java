package br.edu.ufape.poo.backend.exceptions;

public class TakenEmailException extends Exception {
	private static final long serialVersionUID = 1L;

	public TakenEmailException() {
		super("email taken");
	}
}