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

import java.util.List;

import br.com.rede.ke.backoffice.Application;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * The Class PvPermissionServiceIT.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PvPermissionServiceIT {

    /** The pv permission service. */
    @Autowired
    private PvPermissionService pvPermissionService;

    @Autowired
    private PvService pvService;

    @Autowired
    private UserRepository userRepository;

    /** The pageable. */
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
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmail(Acquirer.CIELO, null,
            null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(3));
    }

    /**
     * Test find all by acquirer filters REDE.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirerFiltersREDE() {
        String code = "42345678";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmail(Acquirer.REDE, code,
            null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(0));
    }

    /**
     * Test find all by acquirer and code and email.
     */
    @Test
    @Transactional
    public void testFindAllByAcquirerAndCodeAndEmail() {
        String code = "92315670";
        String email = "bar@foo.com";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmail(Acquirer.CIELO, code,
            email, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(1));
    }

    @Test
    @Transactional
    public void testSavePvPermissionsForUser(){
        String pvs = "1000201314\n101476A6629\n1000201330\n1005B867493\n22345678\n";
        List<Pv> pvList = PvFactory.fromCodesAndAcquirer(pvs, Acquirer.CIELO);
        PvBatch pvBatch = pvService.generatePvBatch(pvList);
        User user = userRepository.findByEmail("foo@bar.com");

        pvPermissionService.savePvPermissionsForUser(pvBatch, user);
    }

}