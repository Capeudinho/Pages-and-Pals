package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ufape.poo.backend.business.entity.Bookshelf;
import java.util.List;

public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {
	public List<Bookshelf> deleteByOwnerId(long ownerId);
	public List<Bookshelf> findByOwnerIdOrderByCreationDateDesc(long id);
	public List<Bookshelf> findByOwnerIdOrderByCreationDateDesc(long id, Pageable pageable);
	public int countByOwnerId(long id);
	@Query("SELECT bookshelf FROM Bookshelf bookshelf WHERE (bookshelf.owner.privacy = true OR bookshelf.owner.id = :ownerId) AND (:bookshelfName IS NOT null OR :ownerName IS NOT null) AND (:bookshelfName IS null OR LOWER(bookshelf.name) LIKE LOWER(CONCAT('%', :bookshelfName, '%'))) AND (:ownerName IS null OR LOWER(bookshelf.owner.name) LIKE LOWER(CONCAT('%', :ownerName, '%')))")
	public List<Bookshelf> findByPublicOrOwnerIdAndOwnerNameAndBookshelfName(@Param("ownerName") String ownerName,
			@Param("bookshelfName") String bookshelfName, @Param("ownerId") long ownerId, Pageable pageable);
@Query("SELECT bookshelf FROM Bookshelf bookshelf WHERE bookshelf.owner.privacy = true AND (:bookshelfName IS NOT null OR :ownerName IS NOT null) AND (:bookshelfName IS null OR LOWER(bookshelf.name) LIKE LOWER(CONCAT('%', :bookshelfName, '%'))) AND (:ownerName IS null OR LOWER(bookshelf.owner.name) LIKE LOWER(CONCAT('%', :ownerName, '%')))")
	public List<Bookshelf> findByPublicAndOwnerNameAndBookshelfName(@Param("ownerName") String ownerName,
			@Param("bookshelfName") String bookshelfName, Pageable pageable);
}