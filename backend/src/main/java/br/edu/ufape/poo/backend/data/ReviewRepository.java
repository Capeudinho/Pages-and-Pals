package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Review;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	public List<Review> deleteByOwnerId(long ownerId);
	public List<Review> findByOwnerIdOrderByCreationDateDesc(long id);
	public List<Review> findByOwnerIdOrderByCreationDateDesc(long id, Pageable pageable);
	@Query("SELECT review FROM Review review WHERE review.bookApiId = :bookApiId AND (review.owner.id = :ownerId OR review.privacy = :privacy) ORDER BY creationDate DESC")
	public List<Review> findByBookApiIdAndPrivacyOrOwnerIdOrderByCreationDateDesc(@Param("bookApiId") String bookApiId,
			@Param("ownerId") long ownerId, @Param("privacy") boolean privacy, Pageable pageable);
	public List<Review> findByBookApiIdAndPrivacyOrderByCreationDateDesc(String id, boolean privacy, Pageable pageable);
	public List<Review> findByOwnerIdAndPrivacyOrderByCreationDateDesc(long id, boolean privacy, Pageable pageable);
	public List<Review> findByBookApiIdOrderByCreationDateDesc(String bookApiId, Pageable pageable);
	public Review findByOwnerIdAndBookApiId(long ownerId, String bookApiId);
	public int countByOwnerId(long id);
	public int countByBookApiId(String id);
}