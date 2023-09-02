package br.edu.ufape.poo.backend.business.service;

import br.edu.ufape.poo.backend.business.entity.Book;

public interface BookServiceInterface {
	public Book findByApiId(String apiId) throws Exception;
	public Double findScoreByApiId(String apiId) throws Exception;
	public Book create(Book book);
	public Book update(Book book) throws Exception;
	public Book deleteByApiId(String apiId) throws Exception;
}