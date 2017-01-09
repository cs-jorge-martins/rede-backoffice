package br.com.rede.ke.backoffice.conciliation.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
}
