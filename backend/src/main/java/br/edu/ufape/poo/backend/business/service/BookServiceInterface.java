package br.edu.ufape.poo.backend.business.service;

import br.edu.ufape.poo.backend.business.entity.Book;

public interface BookServiceInterface
{
	public Book findByApiId(String apiId) throws Exception;
	public Double findScoreByApiId(String apiId) throws Exception;
}