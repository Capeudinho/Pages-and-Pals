package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Review;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	public List<Review> findByOwnerIdOrderByCreationDateDesc(Long id);
	public List<Review> findByOwnerIdOrderByCreationDateDesc(Long id, Pageable pageable);
	public List<Review> findByOwnerIdInOrderByCreationDateDesc(List<Long> ids);
	public List<Review> findByBookApiIdOrderByCreationDateDesc(String bookApiId, Pageable pageable);
    public int countByOwnerId(Long id);
    public int countByBookApiId(String id);
}
