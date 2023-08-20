package br.edu.ufape.poo.backend.business.service;

import java.util.List;

import br.edu.ufape.poo.backend.business.entity.Bookshelf;

public interface BookshelfServiceInterface
{
	public Bookshelf create(Bookshelf bookshelf) throws Exception;
	public Bookshelf update(Bookshelf bookshelf) throws Exception;
	public Bookshelf addBookApiIdById(Long id, String bookApiId) throws Exception;
	public Bookshelf removeBookApiIdById(Long id, String bookApiId) throws Exception;
	public Bookshelf deleteById(Long id) throws Exception;
	public Bookshelf findById(Long id) throws Exception;
	public List<Bookshelf> findByOwnerId(Long id);
	public List<Bookshelf> findByOwnerIdPaginate(Long id, int offset, int limit);
	public int countByOwnerId(Long id);
}