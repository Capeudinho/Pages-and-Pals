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
	@Query("SELECT bookshelf FROM Bookshelf bookshelf WHERE (bookshelf.owner.privacy = true OR bookshelf.owner.id = :ownerId) AND ((EXISTS (SELECT bookApiId FROM bookshelf.bookApiIds bookApiId WHERE bookApiId IN :bookApiIds)) OR ((:bookshelfName IS NOT null OR :ownerName IS NOT null) AND (:bookshelfName IS null OR LOWER(bookshelf.name) LIKE LOWER(CONCAT('%', :bookshelfName, '%'))) AND (:ownerName IS null OR LOWER(bookshelf.owner.name) LIKE LOWER(CONCAT('%', :ownerName, '%')))))")
	public List<Bookshelf> findByAdvancedAutheticated(@Param("ownerName") String ownerName,
			@Param("bookshelfName") String bookshelfName, @Param("bookApiIds") List<String> bookApiIds,
			@Param("ownerId") long ownerId, Pageable pageable);
	@Query("SELECT bookshelf FROM Bookshelf bookshelf WHERE bookshelf.owner.privacy = true AND ((EXISTS (SELECT bookApiId FROM bookshelf.bookApiIds bookApiId WHERE bookApiId IN :bookApiIds)) OR ((:bookshelfName IS NOT null OR :ownerName IS NOT null) AND (:bookshelfName IS null OR LOWER(bookshelf.name) LIKE LOWER(CONCAT('%', :bookshelfName, '%'))) AND (:ownerName IS null OR LOWER(bookshelf.owner.name) LIKE LOWER(CONCAT('%', :ownerName, '%')))))")
	public List<Bookshelf> findByAdvanced(@Param("ownerName") String ownerName,
			@Param("bookshelfName") String bookshelfName, @Param("bookApiIds") List<String> bookApiIds, Pageable pageable);
}