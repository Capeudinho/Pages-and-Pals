package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Review;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByOwnerIdOrderByCreationDateDesc(Long id);
	List<Review> findByOwnerIdOrderByCreationDateDesc(Long id, Pageable pageable);
    //List<Review> findByInOwnerIdOrderByCreationDateDesc(List<Long> ids);
}
