package br.edu.ufape.poo.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.basic.Bookshelf;
import br.edu.ufape.poo.backend.business.basic.Account;
import java.util.List;

@Repository
public interface BookshelfRepository extends JpaRepository<Bookshelf, Long>
{
	public List<Bookshelf> findByOwner(Account owner, Pageable pageable);
}