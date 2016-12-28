/*
 * Copyright 2016 Rede S.A.
 *************************************************************
 * Nome     : AcquirerServiceTest.java
 * Descrição: AcquirerServiceTest.
 * Autor    : Bruno Silva <bruno.fsilva@userede.com.br>
 * Data     : 09/11/2016
 * Empresa  : Rede
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

import br.com.rede.ke.backoffice.Application;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;

/**
 * The Class AcquirerServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PvPermissionFindAllServiceIT {
    
    @Autowired
    private PvPermissionService pvPermissionService;
    
    private Pageable pageable;
    
    @Before
    public void setUp() {
        this.pageable = new PageRequest(0 , 20);
    }

    @Test
    public void testFindAllByAcquirer() {
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmail(Acquirer.CIELO, null, null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(3));
    }
    
    @Test
    public void testFindAllByAcquirerFiltersREDE() {
        String code = "42345678";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmail(Acquirer.REDE, code, null, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(0));
    }
    
    @Test
    public void testFindAllByAcquirerAndCodeAndEmail() {
        String code = "92315670";
        String email = "bar@foo.com";
        Page<PvPermission> searchResults = pvPermissionService.findAllByAcquirerAndCodeAndEmail(Acquirer.CIELO, code, email, pageable);
        assertThat(searchResults.getNumberOfElements(), equalTo(1));
    }

}