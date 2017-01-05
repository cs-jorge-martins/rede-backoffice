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

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PvPermissionRepository pvPermissionRepository;

    @Test
    public void testHasPermissionToHeadquarterPv() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        Pv pv = new Pv();
        pv.setId(1L);
        pv.setCode("code");

        PvPermission pvPermission = new PvPermission();
        pvPermission.setUser(user);
        pvPermission.setPv(pv);

        when(pvPermissionRepository.findByUser(user)).thenReturn(Collections.singleton(pvPermission));

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(true));
    }

    @Test
    public void testHasPermissionToBranchPv() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        Pv headquarterPv = new Pv();
        headquarterPv.setId(1L);
        headquarterPv.setCode("code0");

        Pv branchPv = new Pv();
        branchPv.setId(2L);
        branchPv.setCode("code1");

        branchPv.setHeadquarter(headquarterPv);
        headquarterPv.setBranches(Collections.singleton(branchPv));

        PvPermission pvPermission = new PvPermission();
        pvPermission.setUser(user);
        pvPermission.setPv(branchPv);

        when(pvPermissionRepository.findByUser(user)).thenReturn(Collections.singleton(pvPermission));

        boolean hasAccess = userService.hasAccess(user, branchPv);
        assertThat(hasAccess, equalTo(true));
    }

    @Test
    public void testHasAccessToBranchPv() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");

        Pv headquarterPv = new Pv();
        headquarterPv.setId(2L);
        headquarterPv.setCode("12345");

        Pv branchPv = new Pv();
        branchPv.setId(3L);
        branchPv.setCode("56728");
        branchPv.setHeadquarter(headquarterPv);

        headquarterPv.setBranches(Collections.singleton(branchPv));

        PvPermission pvPermission = new PvPermission();
        pvPermission.setUser(user);
        pvPermission.setPv(headquarterPv);

        when(pvPermissionRepository.findByUser(user)).thenReturn(Collections.singleton(pvPermission));

        boolean hasAccess = userService.hasAccess(user, branchPv);
        assertThat(hasAccess, equalTo(true));
    }
}