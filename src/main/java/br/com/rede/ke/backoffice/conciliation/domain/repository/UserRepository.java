/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserRepository.java
 * Descrição: UserRepository.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find by email.
     *
     * @param email
     *            the email
     * @return the optional
     */
    Optional<User> findByEmail(String email);
}
