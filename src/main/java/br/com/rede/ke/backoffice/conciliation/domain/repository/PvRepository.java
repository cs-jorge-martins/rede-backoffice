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
    List<Pv> findByCodeIn(List<String> pvCodes);
    Pv findByCode(String pvCode);
    Optional<Pv> findByCodeAndAcquirerId(String pvCode, Integer acquirerId);
}
