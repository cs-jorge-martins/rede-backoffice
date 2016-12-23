package br.com.rede.ke.backoffice.conciliador.domain.repository;

import br.com.rede.ke.backoffice.conciliador.domain.entity.Pv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PvRepository extends JpaRepository<Pv, Long> {
}
