package br.edu.ufape.poo.backend.exceptions;

public class DuplicateApiIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateApiIdException() {
		super("duplicate api id");
	}
}