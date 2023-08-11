package br.edu.ufape.poo.backend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufape.poo.backend.business.entity.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> 
{
	public Optional<Book> findByApiId(String apiId); 
	public double findScoreByApiId(String apiId); 
}