package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PvPermissionRepository extends JpaRepository<PvPermission, PvPermissionId> {
    Page<PvPermission> findAll(Specification<PvPermission> specification, Pageable pageable);
}
