/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionServiceTest.java
 * Descrição: PvPermissionServiceTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidSecondaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidPrimaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
import br.com.rede.ke.backoffice.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PvPermissionServiceTest {

    @InjectMocks
    private PvPermissionService pvPermissionService;

    @Mock
    private UserService userService;

    @Mock
    private PvRepository pvRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PvPermissionRepository pvPermissionRepository;

    public static final String PRIMARY_USER_EMAIL = "primary_user@email.com";
    public static final String SECONDARY_USER_EMAIL = "secondary_user@email.com";
    public static final String PV_CODE = "pvcode";

    @Test
    public void testCreateForSecondaryUser() {
        SecondaryUserPvPermissionRequest secondaryUserPvPermissionRequest = new SecondaryUserPvPermissionRequest(
            PRIMARY_USER_EMAIL, SECONDARY_USER_EMAIL, PV_CODE);

        User primaryUser = new User();
        when(userRepository.findByEmail(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser));

        User secondaryUser = new User();
        secondaryUser.setPrimaryUser(primaryUser);
        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        Pv pv = new Pv();
        when(pvRepository.findByCode(PV_CODE)).thenReturn(Optional.of(pv));

        when(userService.hasAccess(primaryUser, pv)).thenReturn(true);

        pvPermissionService.createForSecondaryUser(secondaryUserPvPermissionRequest);

        PvPermission pvPermission = new PvPermission();
        pvPermission.setPv(pv);
        pvPermission.setUser(secondaryUser);

        verify(pvPermissionRepository).save(pvPermission);
    }

    @Test
    public void testCreateForSecondaryWhenRequesterUserHasNoPvPermissionAccess() {
        SecondaryUserPvPermissionRequest secondaryUserPvPermissionRequest = new SecondaryUserPvPermissionRequest(
            PRIMARY_USER_EMAIL, SECONDARY_USER_EMAIL, PV_CODE);

        User primaryUser = new User();
        when(userRepository.findByEmail(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser));

        User secondaryUser = new User();
        secondaryUser.setPrimaryUser(primaryUser);
        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        Pv pv = new Pv();
        when(pvRepository.findByCode(PV_CODE)).thenReturn(Optional.of(pv));

        when(userService.hasAccess(primaryUser, pv)).thenReturn(false);

        Result<PvPermission, String> result = pvPermissionService.createForSecondaryUser(secondaryUserPvPermissionRequest);
        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(), equalTo("User 'null' has no access to Pv 'null'"));
    }

    @Test(expected = InvalidPrimaryUserException.class)
    public void testCreateForSecondaryWhenRequesterUserIsSecondaryUser() {
        SecondaryUserPvPermissionRequest secondaryUserPvPermissionRequest = new SecondaryUserPvPermissionRequest(
            SECONDARY_USER_EMAIL, PRIMARY_USER_EMAIL, PV_CODE);

        User secondaryUser = mock(User.class);
        when(secondaryUser.isPrimary()).thenReturn(false);
        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        pvPermissionService.createForSecondaryUser(secondaryUserPvPermissionRequest);
    }

    @Test(expected = InvalidSecondaryUserException.class)
    public void testCreateForSecondaryWhenToBePermittedUserIsNotSecondaryUserOfRequesterUser() {
        SecondaryUserPvPermissionRequest secondaryUserPvPermissionRequest = new SecondaryUserPvPermissionRequest(
            PRIMARY_USER_EMAIL, SECONDARY_USER_EMAIL, PV_CODE);

        User primaryUser = mock(User.class);
        when(primaryUser.isPrimary()).thenReturn(true);
        when(userRepository.findByEmail(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser));

        User secondaryUser = mock(User.class);
        when(secondaryUser.isPrimary()).thenReturn(false);
        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        when(primaryUser.isPrimaryOf(secondaryUser)).thenReturn(false);

        when(pvRepository.findByCode(PV_CODE)).thenReturn(Optional.of(new Pv()));

        pvPermissionService.createForSecondaryUser(secondaryUserPvPermissionRequest);
    }

    @Test(expected = InvalidSecondaryUserException.class)
    public void testCreateForSecondaryWhenToBePermittedUserIsPrimaryUser() {
        SecondaryUserPvPermissionRequest secondaryUserPvPermissionRequest = new SecondaryUserPvPermissionRequest(
            PRIMARY_USER_EMAIL, SECONDARY_USER_EMAIL, PV_CODE);

        User primaryUser = mock(User.class);
        when(primaryUser.isPrimary()).thenReturn(true);
        when(userRepository.findByEmail(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser));

        User secondaryUser = mock(User.class);
        when(secondaryUser.isPrimary()).thenReturn(true);
        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        pvPermissionService.createForSecondaryUser(secondaryUserPvPermissionRequest);
    }
}