/*
 * Copyright 2016 Rede S.A.
 *************************************************************
 * Nome     : PvServiceIT.java
 * Descrição: PvServiceIT.
 * Autor    : Karlisson Bezerra <kbezerra@thoughtworks.com>
 * Data     : 03/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.domain.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.rede.ke.backoffice.Application;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;

/**
 * The Class PvServiceIT.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PvServiceIT {
    
    /** The pv service. */
    @Autowired
    private PvService pvService;
    
    @Test
    @Transactional
    public void testPvBatchValidation(){
        String pvs = "1000201314\n101476A6629\n1000201330\n1005B867493\n22345678\n";
        List<Pv> pvList = PvFactory.fromString(pvs);
        
        PvBatch pvBatch = pvService.generatePvBatch(pvList);
        
        assertThat(pvBatch.getValidPvs().size(), equalTo(2));
        assertThat(pvBatch.getInvalidPvs().size(), equalTo(3));
    }
}