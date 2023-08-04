package br.edu.ufape.poo.backend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Book;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>
{
	public Optional<Book> findByApiId(String apiId);
}