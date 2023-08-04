package br.edu.ufape.poo.backend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.entity.Account;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
	public Optional<Account> findByEmail(String email);
	public Optional<Account> findByEmailAndPassword(String email, String password);
}