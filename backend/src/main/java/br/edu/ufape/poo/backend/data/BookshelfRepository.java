package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import java.util.List;

@Repository
public interface BookshelfRepository extends JpaRepository<Bookshelf, Long>
{
	public List<Bookshelf> findByOwnerIdOrderByCreationDateDesc(Long id);
	public List<Bookshelf> findByOwnerIdOrderByCreationDateDesc(Long id, Pageable pageable);
	public List<Bookshelf> deleteByOwnerId(Long ownerId);
	public int countByOwnerId(Long id);
}