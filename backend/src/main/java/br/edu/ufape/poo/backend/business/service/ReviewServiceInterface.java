package br.edu.ufape.poo.backend.business.service;

import java.util.List;
import br.edu.ufape.poo.backend.business.entity.Review;

public interface ReviewServiceInterface
{
	public Review create(Review review)throws Exception;
	public Review findById(long id) throws Exception;
	public List<Review> findByOwnerIdPaginate(long id, int offset, int limit);
	public Review update(Review review) throws Exception ;
	public Review deleteById (long id) throws Exception;
	public int countByOwnerId(long id);
	public List<Review> findByBookApiIdPaginate(String bookApiId, int offset, int limit);
	public int countByBookApiId(String id);
}
