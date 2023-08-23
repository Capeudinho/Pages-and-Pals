package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import java.util.List;

public interface BookshelfRepository extends JpaRepository<Bookshelf, Long>
{
	public List<Bookshelf> findByOwnerIdOrderByCreationDateDesc(Long id);
	public List<Bookshelf> findByOwnerIdOrderByCreationDateDesc(Long id, Pageable pageable);
	public int countByOwnerId(Long id);

	@Query("SELECT bookshelf FROM Bookshelf bookshelf WHERE (:bookshelfName IS NOT null OR :ownerName IS NOT null) AND (:bookshelfName IS null OR bookshelf.name LIKE %:bookshelfName%) AND (:ownerName IS null OR bookshelf.owner.name LIKE %:ownerName%)")
	public List<Object> findByOwnerAndBookshelfName(@Param("ownerName") String ownerName, @Param("bookshelfName") String bookshelfName);

}