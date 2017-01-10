/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserServiceTest.java
 * Descrição: UserServiceTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 03/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.Arrays;
import java.util.Collections;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The UserServiceTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PvPermissionRepository pvPermissionRepository;

    private User user;
    private Pv pv;

    @Before
    public void setUp() {
        user = new User();
        pv = new Pv();
    }

    /**
     * Has access when user has no permission.
     */
    @Test
    public void hasAccessWhenUserHasNoPermission() {
        when(pvPermissionRepository.findByUser(user)).thenReturn(Collections.emptyList());

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(false));
    }

    /**
     * Has access when user has permission that not allow pv access.
     */
    @Test
    public void hasAccessWhenUserHasPermissionThatNotAllowPvAccess() {
        PvPermission pvPermission = mock(PvPermission.class);
        when(pvPermission.permitAccess(pv)).thenReturn(false);
        when(pvPermissionRepository.findByUser(user)).thenReturn(Arrays.asList(pvPermission));

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(false));
    }

    /**
     * Has access when user has permission that allow pv access.
     */
    @Test
    public void hasAccessWhenUserHasPermissionThatAllowPvAccess() {
        PvPermission pvPermission = mock(PvPermission.class);
        when(pvPermission.permitAccess(pv)).thenReturn(true);
        when(pvPermissionRepository.findByUser(user)).thenReturn(Arrays.asList(pvPermission));

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(true));
    }
}