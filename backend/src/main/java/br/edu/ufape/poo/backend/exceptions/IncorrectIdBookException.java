package br.edu.ufape.poo.backend.exceptions;

public class IncorrectIdBookException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public IncorrectIdBookException() {
		super ("incorrect book id");
	}

}
