package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PvRepository extends JpaRepository<Pv, Long> {
    Page<Pv> findAll(Specification<Pv> specification, Pageable pageable);
}
