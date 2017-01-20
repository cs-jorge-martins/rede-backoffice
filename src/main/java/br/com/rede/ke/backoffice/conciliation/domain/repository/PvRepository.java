/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvRepository.java
 * Descrição: PvRepository.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The PvRepository class.
 */
@Repository
public interface PvRepository extends JpaRepository<Pv, Long> {

    /**
     * Find by code in.
     *
     * @param pvCodes
     *            the pv codes
     * @return the list
     */
    List<Pv> findByCodeIn(List<String> pvCodes);

    /**
     * Find by code.
     *
     * @param pvCode
     *            the pv code
     * @return the pv
     */
    Pv findByCode(String pvCode);

    /**
     * Find by code and acquirer id.
     *
     * @param pvCode
     *            the pv code
     * @param acquirerId
     *            the acquirer id
     * @return the optional
     */
    Optional<Pv> findByCodeAndAcquirerId(String pvCode, Integer acquirerId);
}
