/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserDetailsServiceImpl.java
 * Descrição: UserDetailsServiceImpl.java.
 * Autor    : Dayany Espindola <dcortes@thoughtworks.com>
 * Data     : 19/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.authentication.domain.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.authentication.domain.entity.AdminUser;
import br.com.rede.ke.backoffice.authentication.domain.repository.AdminUserRepository;

import java.util.Collections;

/**
 * The Class UserDetailsServiceImpl.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AdminUserRepository adminUserRepository;

    /**
     * Instantiates a new user details service impl.
     *
     * @param adminUserRepository the admin user repository
     */
    public UserDetailsServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username or password invalid!"));
        return new User(adminUser.getName(), adminUser.getPassword(), Collections.emptyList());
    }
}
