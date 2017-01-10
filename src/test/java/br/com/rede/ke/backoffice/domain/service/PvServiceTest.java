/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvServiceTest.java
 * Descrição: PvServiceTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.domain.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Class PvServiceTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class PvServiceTest {
    
    /** The pv service. */
    @InjectMocks
    private PvService pvService;
    
    /** The pv repository. */
    @Mock
    private PvRepository pvRepository;
    
    /**
     * Test valid pv.
     */
    @Test
    public void testValidPv(){
        Pv pv = new Pv();
        pv.setCode("1000201314");
        
        assertThat(pvService.isValidPvFormat(pv), equalTo(true));
    }
    
    /**
     * Test pv invalid length.
     */
    @Test
    public void testPvInvalidLength(){
        Pv pv = new Pv();
        pv.setCode("100020131419817181781781718");
        
        assertThat(pvService.isValidPvFormat(pv), equalTo(false));
    }
    
    /**
     * Test invalid characters pv.
     */
    @Test
    public void testInvalidCharactersPv(){
        Pv pv = new Pv();
        pv.setCode("ABC5367");
        
        assertThat(pvService.isValidPvFormat(pv), equalTo(false));
    }
    
    /**
     * Test read pvsfrom string.
     */
    @Test
    public void testReadPvsfromString() throws IOException {
        String pvs = "1000201314\n1014766629\n1000201330\n1005867493\n";
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(pvs.getBytes()));

        List<Pv> pvList = PvFactory.fromCodesAndAcquirer(multipartFile, Acquirer.CIELO);
        
        assertThat(pvList.size(), equalTo(4));
    }
}
