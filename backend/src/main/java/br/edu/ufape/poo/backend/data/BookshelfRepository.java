package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import java.util.List;

@Repository
public interface BookshelfRepository extends JpaRepository<Bookshelf, Long>
{
	public List<Bookshelf> findByOwnerId(Long id);
	public List<Bookshelf> findByOwnerId(Long id, Pageable pageable);
	public List<Bookshelf> findByOwnerIdAndPrivacy(Long id, boolean privacy, Pageable pageable);
	public int countByOwnerId(Long id);
}