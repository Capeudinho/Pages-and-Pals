package br.edu.ufape.poo.backend.business.service;

import java.util.List;

import br.edu.ufape.poo.backend.business.entity.Bookshelf;

public interface BookshelfServiceInterface {
	public Bookshelf create(Bookshelf bookshelf) throws Exception;
	public Bookshelf update(Bookshelf bookshelf) throws Exception;
	public Bookshelf addBookApiIdById(long id, String bookApiId) throws Exception;
	public Bookshelf removeBookApiIdById(long id, String bookApiId) throws Exception;
	public Bookshelf deleteById(long id) throws Exception;
	public List<Bookshelf> deleteByOwnerId(long id) throws Exception;
	public Bookshelf findById(long id) throws Exception;
	public List<Bookshelf> findByOwnerId(long id);
	public List<Bookshelf> findByOwnerIdPaginate(long id, int offset, int limit);
	public List<Object> findByOwnerNameAndBookshelfName(String ownerName, String bookshelfName, int offset, int limit);
	public int countByOwnerId(long id);
}