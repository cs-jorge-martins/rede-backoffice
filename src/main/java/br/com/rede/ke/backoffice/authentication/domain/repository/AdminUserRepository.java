/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : AdminUserRepository.java
 * Descrição: AdminUserRepository.java.
 * Autor    : Dayany Espindola <dcortes@thoughtworks.com>
 * Data     : 19/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.authentication.domain.repository;

import org.springframework.stereotype.Repository;

import br.com.rede.ke.backoffice.authentication.domain.entity.AdminUser;

import java.util.Optional;

/**
 * The Class AdminUserRepository.
 */
@Repository
public class AdminUserRepository {

    private AdminUser adminUser;

    /**
     * Instantiates a new admin user repository.
     *
     * @param user the user
     */
    public AdminUserRepository(AdminUser user) {
        this.adminUser = user;
    }

    /**
     * Find by name.
     *
     * @param username the username
     * @return the optional
     */
    public Optional<AdminUser> findByName(String username) {
        if (adminUser.getName().equals(username)) {
            return Optional.of(adminUser);
        }

        return Optional.empty();
    }
}
