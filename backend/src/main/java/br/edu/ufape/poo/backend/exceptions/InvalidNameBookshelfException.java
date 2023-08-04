package br.edu.ufape.poo.backend.exceptions;

import br.edu.ufape.poo.backend.business.entity.Bookshelf;

public class InvalidNameBookshelfException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Bookshelf bookshelf;
	
	public InvalidNameBookshelfException(Bookshelf bookshelf)
	{
		super ("invalid name");
		this.bookshelf = bookshelf;
	}
	
	public Bookshelf getBookshelf()
	{
		return this.bookshelf;
	}
}