package br.edu.ufape.poo.backend.business.service;

import java.util.List;
import br.edu.ufape.poo.backend.business.entity.Review;

public interface ReviewServiceInterface
{
	public Review create(Review review)throws Exception;
	public Review findById(Long id) throws Exception;
	public List<Review> findByOwnerIdPaginate(Long id, int offset, int limit);
	public Review edit (Review review) throws Exception;
	public Review deleteById (Long id) throws Exception;
	public int countByOwnerId(Long id);
}
