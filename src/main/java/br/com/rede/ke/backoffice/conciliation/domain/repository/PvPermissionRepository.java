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

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;

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

    /**
     * Find by user.
     *
     * @param user
     *            the user
     * @return the list
     */
    List<PvPermission> findByUser(User user);

    /**
     * Find by user and pv.
     *
     * @param user
     *            the user
     * @param pv
     *            the pv
     * @return the optional of a pv permission
     */
    Optional<PvPermission> findByUserAndPv(User user, Pv pv);

    /**
     * Find all by pv.
     *
     * @param pv
     *            the pv
     * @return the list
     */
    List<PvPermission> findAllByPv(Pv pv);

    /**
     * Find all by pv.
     *
     * @param pv
     *            the pv
     * @return the list
     */
    List<PvPermission> findAllByPvHeadquarterRede(Pv pvHeadquarterRede);

    /**
     * Find all by pv in.
     *
     * @param pvs
     *            the pv list
     * @return the list
     */
    List<PvPermission> findAllByPvIn(Iterable<Pv> pvs);

    /**
     * Find by user and pv and pv headquarter Rede.
     *
     * @param primaryUser
     *            the primary user
     * @param headquarter
     *            the headquarter
     * @param pvHeadquarterRede
     *            the pv headquarter rede
     * @return the optional of a pv permission
     */
    Optional<PvPermission> findByUserAndPvAndPvHeadquarterRede(User primaryUser, Pv headquarter, Pv pvHeadquarterRede);
}
