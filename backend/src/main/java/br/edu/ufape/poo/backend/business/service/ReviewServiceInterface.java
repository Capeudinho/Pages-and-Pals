package br.edu.ufape.poo.backend.business.service;

import java.util.List;
import br.edu.ufape.poo.backend.business.entity.Review;

public interface ReviewServiceInterface {
	public Review create(Review review) throws Exception;
	public Review update(Review review) throws Exception;
	public Review deleteById(long id) throws Exception;
	public List<Review> deleteByOwnerId(long ownerId) throws Exception;
	public Review findById(long id) throws Exception;
	public List<Review> findByOwnerIdPaginate(long id, int offset, int limit);
	public List<Review> findByOwnerIdAndPublicPaginate(long id, int offset, int limit);
	public List<Review> findByBookApiIdAndPublicOrOwnerIdPaginate(String bookApiId, long ownerId, int offset,
			int limit);
	public List<Review> findByBookApiIdAndPublicPaginate(String bookApiId, int offset, int limit);
	public int countByOwnerId(long id);
	public int countByBookApiId(String id);
}