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
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

/**
 * The Class PvPermissionServiceTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class PvPermissionServiceTest {

    /** The pv permission service */
    @InjectMocks
    private PvPermissionService pvPermissionService;

    /** The user service */
    @Mock
    private UserService userService;

    /** The pv repository */
    @Mock
    private PvRepository pvRepository;

    /** The pv permission repository */
    @Mock
    private PvPermissionRepository pvPermissionRepository;

    /** The primary user email constant */
    private static final String PRIMARY_USER_EMAIL = "primary_user@email.com";

    /** The secondary user email constant */
    private static final String SECONDARY_USER_EMAIL = "secondary_user@email.com";

    /** The pv code constant */
    private static final String PV_CODE = "pvcode";

    /** The primary user */
    private User primaryUser;

    /** The secondary user */
    private User secondaryUser;

    /** The pv */
    private Pv pv;

    /** The secondary user pv permission request */
    private SecondaryUserPvPermissionRequest pvPermissionRequest;

    /**
     * The setUp
     */
    @Before
    public void setUp() {
        this.pvPermissionRequest = new SecondaryUserPvPermissionRequest(
                PRIMARY_USER_EMAIL, SECONDARY_USER_EMAIL, PV_CODE, Acquirer.CIELO);

        primaryUser = new User();
        when(userService.getPrimaryUser(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser));

        secondaryUser = new User();
        secondaryUser.setPrimaryUser(primaryUser);
        when(userService.getOrCreateSecondaryUserFor(primaryUser, SECONDARY_USER_EMAIL)).thenReturn(secondaryUser);

        pv = new Pv();
        when(pvRepository.findByCodeAndAcquirerId(PV_CODE, pvPermissionRequest.getAcquirer().ordinal()))
                .thenReturn(Optional.of(pv));
    }

    /**
     * Test create for secondary user.
     */
    @Test
    public void testCreateForSecondaryUser() {
        when(userService.hasAccess(primaryUser, pv)).thenReturn(true);

        Result<PvPermission, String> pvPermissionResult = pvPermissionService.createForSecondaryUser(pvPermissionRequest);

        PvPermissionId id = new PvPermissionId(secondaryUser.getId(), pv.getId());
        PvPermission pvPermission = new PvPermission(id, secondaryUser, pv);

        assertThat(pvPermissionResult.isSuccess(), equalTo(true));
        assertThat(pvPermissionResult.success().get(), equalTo(pvPermission));
    }

    /**
     * Test create for secondary user when requester user has no pv permission access
     */
    @Test
    public void testCreateForSecondaryUserWhenRequesterUserHasNoPvPermissionAccess() {
        when(userService.hasAccess(primaryUser, pv)).thenReturn(false);

        Result<PvPermission, String> result = pvPermissionService.createForSecondaryUser(pvPermissionRequest);
        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(), equalTo("Usuário 'null' não tem acesso ao Pv 'null'"));
    }

    /**
     * Test create for secondary user when pv do not exists.
     */
    @Test
    public void testCreateForSecondaryUserWhenPvDoNotExists() {
        when(pvRepository.findByCodeAndAcquirerId(PV_CODE, pvPermissionRequest.getAcquirer().ordinal()))
                .thenReturn(Optional.empty());

        Result<PvPermission, String> result = pvPermissionService.createForSecondaryUser(pvPermissionRequest);

        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(), equalTo(String.format("Pv '%s' não existe", PV_CODE)));
    }

    /**
     * Test create for secondary user when primary user not exists.
     */
    @Test(expected = UserNotFoundException.class)
    public void testCreateForSecondaryUserWhenPrimaryUserNotExists() {
        when(userService.getPrimaryUser(PRIMARY_USER_EMAIL)).thenReturn(Optional.empty());
        pvPermissionService.createForSecondaryUser(pvPermissionRequest);
    }
}