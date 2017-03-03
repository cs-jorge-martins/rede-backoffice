/*
 * Copyright 2016 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionServiceIT.java
 * Descrição: PvPermissionServiceIT.
 * Autor    : Karlisson Bezerra <kbezerra@thoughtworks.com>
 * Data     : 28/12/2016
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.domain.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.rede.ke.backoffice.Application;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;

/**
 * The Class PvPermissionServiceIT.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PvPermissionServiceIT {

    /**
     * The pv permission service.
     */
    @Autowired
    private PvPermissionService pvPermissionService;

    /**
     * The pageable.
     */
    private Pageable pageable;

    /**
     * Setup tests.
     */
    @Before
    public void setUp() {
        this.pageable = new PageRequest(0, 20);
    }

    /**
     * Test find all by acquirer.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirer() {
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede(
            Acquirer.CIELO, null,
            null, null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(3));
    }

    /**
     * Test find all by acquirer filters REDE.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirerFiltersREDE() {
        String code = "42345678";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede(
            Acquirer.REDE, code,
            null, null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(0));
    }

    /**
     * Test find all by acquirer and code and email.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirerAndCodeAndEmail() {
        String code = "0092315670";
        String email = "bar@foo.com";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede(
            Acquirer.CIELO, code,
            email, null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(1));
    }

    /**
     * Test find all by acquirer and code and email and pv headquarter rede.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede() {
        String code = "0092315670";
        String email = "bar@foo.com";
        String pvHeadquarterRedeCode = "12345678";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede(
            Acquirer.CIELO, code,
            email, pvHeadquarterRedeCode, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(1));
    }

    /**
     * Test find all by acquirer and email and pv headquarter rede.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirerAndEmailAndPvHeadquarterRede() {
        String email = "bar@foo.com";
        String pvHeadquarterRedeCode = "12345678";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede(
            Acquirer.CIELO, null,
            email, pvHeadquarterRedeCode, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(2));
    }
}