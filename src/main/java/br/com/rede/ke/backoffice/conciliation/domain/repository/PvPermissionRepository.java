/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionRepository.java
 * Descrição: PvPermissionRepository.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface PvPermissionRepository.
 */
@Repository
public interface PvPermissionRepository extends JpaRepository<PvPermission, PvPermissionId> {

    /**
     * Find all.
     *
     * @param specification
     *            the specification
     * @param pageable
     *            the pageable
     * @return the page
     */
    Page<PvPermission> findAll(Specification<PvPermission> specification, Pageable pageable);
}
