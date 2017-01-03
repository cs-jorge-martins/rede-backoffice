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
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.rede.ke.backoffice.Application;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
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
    public void testValidMasterPv(){
        Pv pv = new Pv();
        pv.setCode("12345678");
        
        List<Pv> pvs = pvService.findHeadquarterPv(pv);
        
        assertThat(pvs.size(), equalTo(1));
        
    }
    
    @Test
    public void testPvBatchValidation(){
        String pvs = "1000201314\n101476A6629\n1000201330\n1005B867493\n";
        List<Pv> pvList = pvService.readPvsFromString(pvs);
        
        PvBatch batch = pvService.processPvBatch(pvList);
        
        List<String> pvCodes = batch.getSucessfulPvs().stream().map(Pv::getCode).collect(Collectors.toList());
        
        List<Pv> sucessfulPvs = pvService.findByPvCodes(pvCodes);
        
        assertThat(sucessfulPvs.size(), equalTo(2));
        assertThat(batch.getFailedPvs().size(), equalTo(2));
    }

}