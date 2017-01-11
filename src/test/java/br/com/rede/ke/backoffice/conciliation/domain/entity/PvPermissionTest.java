/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionTest.java
 * Descrição: PvPermissionTest.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 05/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import java.util.Collections;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
public class PvPermissionTest {

    /**
     * Permit access to headquarterPv.
     */
    @Test
    public void permitAccessHeadquarterPv() {
        Pv headquarterPv = new Pv();
        headquarterPv.setBranches(Collections.emptySet());

        PvPermission pvPermission = new PvPermission();
        pvPermission.setPv(headquarterPv);

        boolean hasAccess = pvPermission.permitAccess(headquarterPv);

        assertThat(hasAccess, equalTo(true));
    }

    /**
     * Permit access branchPv.
     */
    @Test
    public void permitAccessBranchPv() {
        Pv headquarterPv = new Pv();
        Pv branchPv = new Pv();

        branchPv.setHeadquarter(headquarterPv);
        headquarterPv.setBranches(Collections.singleton(branchPv));

        PvPermission pvPermission = new PvPermission();
        pvPermission.setPv(headquarterPv);

        boolean hasAccess = pvPermission.permitAccess(branchPv);

        assertThat(hasAccess, equalTo(true));
    }

    /**
     * Permit access when pvs are null.
     */
    @Test
    public void permitAccessWhenPvsAreNull() {
        PvPermission pvPermission = new PvPermission();

        boolean hasAccess = pvPermission.permitAccess(null);

        assertThat(hasAccess, equalTo(false));
    }

    /**
     * Permit access when branches are null.
     */
    @Test
    public void permitAccessWhenBranchesAreNull() {
        PvPermission pvPermission = new PvPermission();
        Pv pv1 = new Pv();
        pv1.setId(1L);

        pvPermission.setPv(pv1);

        Pv pv2 = new Pv();
        pv2.setId(2L);

        boolean hasAccess = pvPermission.permitAccess(pv2);

        assertThat(hasAccess, equalTo(false));
    }
}