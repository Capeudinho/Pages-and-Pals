package br.edu.ufape.poo.backend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.poo.backend.business.basic.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
	
}