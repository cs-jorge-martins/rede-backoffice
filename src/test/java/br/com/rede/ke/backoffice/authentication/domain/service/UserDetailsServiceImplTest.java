/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserDetailsServiceImplTest.java
 * Descrição: UserDetailsServiceImplTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.authentication.domain.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.rede.ke.backoffice.authentication.domain.entity.AdminUser;
import br.com.rede.ke.backoffice.authentication.domain.repository.AdminUserRepository;

/**
 * The Class UserDetailsServiceImplTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    /** The Constant VALID_USERNAME. */
    private static final String VALID_USERNAME = "root";

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    /** The user repository. */
    @Mock
    private AdminUserRepository userRepository;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByName(VALID_USERNAME)).thenReturn(Optional.of(createAdminUser()));
    }

    /**
     * Test load user by username given valid username.
     */
    @Test
    public void testLoadUserByUsernameGivenValidUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(VALID_USERNAME);
        assertThat(userDetails.getUsername(), equalTo(VALID_USERNAME));
    }

    /**
     * Test load user by username given invalid username.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameGivenInvalidUsername() {
        userDetailsService.loadUserByUsername("invalid_username");
    }

    private AdminUser createAdminUser() {
        AdminUser user = new AdminUser();
        user.setName(VALID_USERNAME);
        user.setPassword("password");
        return user;
    }
}