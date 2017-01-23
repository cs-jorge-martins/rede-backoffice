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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import br.com.rede.ke.backoffice.conciliation.domain.PrimaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.UserNotFoundException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.util.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * The Class PvPermissionServiceTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class PvPermissionServiceTest {

    /** The primary user email constant */
    private static final String PRIMARY_USER_EMAIL = "primary_user@email.com";

    /** The secondary user email constant */
    private static final String SECONDARY_USER_EMAIL = "secondary_user@email.com";

    /** The pv code constant */
    private static final String PV_CODE = "pvcode";

    /** The pv permission service */
    @InjectMocks
    private PvPermissionService pvPermissionService;

    /** The user service */
    @Mock
    private UserService userService;

    /** The pv service */
    @Mock
    private PvService pvService;

    /** The pv repository */
    @Mock
    private PvRepository pvRepository;

    /** The pv permission repository */
    @Mock
    private PvPermissionRepository pvPermissionRepository;

    /** The primary user */
    private User primaryUser;

    /** The secondary user */
    private User secondaryUser;

    /** The pv */
    private Pv pv;

    /** The primary user pv permission request */
    private PrimaryUserPvPermissionRequest primaryUserPvPermissionRequest;

    /** The primary user pv permission */
    private PvPermission primaryUserPvPermission;

    /** The secondary user pv permission */
    private PvPermission secondaryUserPvPermission;

    /** The secondary user pv permission request */
    private SecondaryUserPvPermissionRequest secondaryUserPvPermissionRequest;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        this.primaryUserPvPermissionRequest = new PrimaryUserPvPermissionRequest(
            PRIMARY_USER_EMAIL, PV_CODE, Acquirer.CIELO);

        this.secondaryUserPvPermissionRequest = new SecondaryUserPvPermissionRequest(
            PRIMARY_USER_EMAIL, SECONDARY_USER_EMAIL, PV_CODE, Acquirer.CIELO);

        primaryUser = new User();
        when(userService.getPrimaryUser(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser));
        when(pvService.isValidPv(Mockito.any())).thenReturn(true);

        secondaryUser = new User();
        secondaryUser.setPrimaryUser(primaryUser);
        when(userService.getOrCreateSecondaryUserFor(primaryUser, SECONDARY_USER_EMAIL)).thenReturn(secondaryUser);

        pv = new Pv();
        when(pvRepository.findByCodeAndAcquirerId(PV_CODE, secondaryUserPvPermissionRequest.getAcquirer().ordinal()))
            .thenReturn(Optional.of(pv));

        primaryUserPvPermission = new PvPermission(primaryUser, pv);
        secondaryUserPvPermission = new PvPermission(secondaryUser, pv);
    }

    /**
     * Test create for secondary user.
     */
    @Test
    public void testCreateForSecondaryUser() {
        when(userService.hasAccess(primaryUser, pv)).thenReturn(true);

        Result<PvPermission, String> pvPermissionResult = pvPermissionService
            .createForSecondaryUser(secondaryUserPvPermissionRequest);

        PvPermissionId id = new PvPermissionId(secondaryUser.getId(), pv.getId());
        PvPermission pvPermission = new PvPermission(id, secondaryUser, pv);

        assertThat(pvPermissionResult.isSuccess(), equalTo(true));
        assertThat(pvPermissionResult.success().get(), equalTo(pvPermission));
    }

    /**
     * Test create for secondary user when requester user has no pv permission
     * access.
     */
    @Test
    public void testCreateForSecondaryUserWhenRequesterUserHasNoPvPermissionAccess() {
        when(userService.hasAccess(primaryUser, pv)).thenReturn(false);

        Result<PvPermission, String> result = pvPermissionService
            .createForSecondaryUser(secondaryUserPvPermissionRequest);
        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(), equalTo("Usuário 'null' não tem acesso ao Pv 'null'"));
    }

    /**
     * Test create for secondary user when pv do not exists.
     */
    @Test
    public void testCreateForSecondaryUserWhenPvDoNotExists() {
        when(pvRepository.findByCodeAndAcquirerId(PV_CODE, secondaryUserPvPermissionRequest.getAcquirer().ordinal()))
            .thenReturn(Optional.empty());

        Result<PvPermission, String> result = pvPermissionService
            .createForSecondaryUser(secondaryUserPvPermissionRequest);

        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(), equalTo(String.format("Pv '%s' não existe", PV_CODE)));
    }

    /**
     * Test create for secondary user when primary user not exists.
     */
    @Test(expected = UserNotFoundException.class)
    public void testCreateForSecondaryUserWhenPrimaryUserNotExists() {
        when(userService.getPrimaryUser(PRIMARY_USER_EMAIL)).thenReturn(Optional.empty());
        pvPermissionService.createForSecondaryUser(secondaryUserPvPermissionRequest);
    }

    /**
     * Test create for secondary user when pv has invalid format.
     */
    @Test
    public void testCreateForSecondaryUserWhenPvHasInvalidFormat() {
        when(pvRepository.findByCodeAndAcquirerId(PV_CODE, secondaryUserPvPermissionRequest.getAcquirer().ordinal()))
                .thenReturn(Optional.empty());
        when(pvService.isValidPv(Mockito.any())).thenReturn(false);

        Result<PvPermission, String> result = pvPermissionService
            .createForSecondaryUser(secondaryUserPvPermissionRequest);

        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(), equalTo(String.format("Pv '%s' com formato invalido", PV_CODE)));
    }

    /**
     * Test delete pv permission from primary user.
     */
    @Test
    public void testDeletePvPermissionFromPrimaryUser() {
        List<PvPermission> permissions = Arrays.asList(primaryUserPvPermission, secondaryUserPvPermission);
        when(pvPermissionRepository.findAllByPv(pv)).thenReturn(permissions);

        pvPermissionService.delete(primaryUserPvPermission);

        verify(pvPermissionRepository).delete(permissions);
    }

    /**
     *  Test delete pv permission from secondary user.
     */
    @Test
    public void testDeletePvPermissionFromSecondaryUser() {
        List<PvPermission> permissions = Arrays.asList(primaryUserPvPermission, secondaryUserPvPermission);
        when(pvPermissionRepository.findAllByPv(pv)).thenReturn(permissions);

        pvPermissionService.delete(secondaryUserPvPermission);

        verify(pvPermissionRepository).delete(secondaryUserPvPermission);
    }

    /**
     * Test delete pv permission from primary user when secondary user has pv permission to branch pv.
     */
    @Test
    public void testDeletePvPermissionFromPrimaryUserWhenSecondaryUserHasPvPermissionToBranchPv() {
        Pv branchPv = new Pv();
        branchPv.setHeadquarter(pv);
        pv.setBranches(Arrays.asList(branchPv));

        secondaryUserPvPermission = new PvPermission(secondaryUser, branchPv);

        when(pvPermissionRepository.findAllByPv(pv))
                .thenReturn(Arrays.asList(primaryUserPvPermission));

        when(pvPermissionRepository.findAllByPvIn(pv.getBranches()))
                .thenReturn(Arrays.asList(secondaryUserPvPermission));

        pvPermissionService.delete(primaryUserPvPermission);

        verify(pvPermissionRepository).delete(Arrays.asList(primaryUserPvPermission, secondaryUserPvPermission));
    }
}
