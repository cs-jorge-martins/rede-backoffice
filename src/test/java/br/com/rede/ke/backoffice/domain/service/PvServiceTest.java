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

import java.util.Optional;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

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
     * Test is valid pv format when code is numbers with size less or equal 20.
     */
    @Test
    public void testIsValidPvFormatWhenCodeIsNumbersWithSizeLessOrEqual20() {
        Pv pv = new Pv();
        pv.setCode("1000201314");

        assertThat(pvService.isValidPvFormat(pv), equalTo(true));
    }

    /**
     * Test is valid pv format when code has invalid length.
     */
    @Test
    public void testIsValidPvFormatWhenCodeHasInvalidLength() {
        Pv pv = new Pv();
        pv.setCode("100020131419817181781781718");

        assertThat(pvService.isValidPvFormat(pv), equalTo(false));
    }

    /**
     * Test is valid pv format when code has invalid characters.
     */
    @Test
    public void testIsValidPvFormatWhenCodeHasInvalidCharacters() {
        Pv pv = new Pv();
        pv.setCode("ABC5367");

        assertThat(pvService.isValidPvFormat(pv), equalTo(false));
    }

    /**
     * Test is valid when pv does not exist.
     */
    @Test
    public void testIsValidWhenPvDoesNotExist() {
        String code = "12345678";
        Acquirer acquirer = Acquirer.CIELO;

        when(pvRepository.findByCodeAndAcquirerId(code, acquirer.ordinal())).thenReturn(Optional.empty());

        assertThat(pvService.isValidPv(new Pv(code, acquirer)), equalTo(true));
    }

    /**
     * Test is valid when pv exists and is headquarter.
     */
    @Test
    public void testIsValidWhenPvExistsAndIsHeadquarter() {
        String code = "12345678";
        Acquirer acquirer = Acquirer.CIELO;

        Pv headquarterPv = new Pv(code, acquirer);

        when(pvRepository.findByCodeAndAcquirerId(code, acquirer.ordinal())).thenReturn(Optional.of(headquarterPv));

        assertThat(pvService.isValidPv(headquarterPv), equalTo(true));
    }

    /**
     * Test is valid when pv exists but is not headquarter.
     */
    @Test
    public void testIsValidWhenPvExistsButIsNotHeadquarter() {
        String code = "12345678";
        Acquirer acquirer = Acquirer.CIELO;

        Pv branchPv = new Pv(code, acquirer);
        branchPv.setHeadquarter(new Pv());

        when(pvRepository.findByCodeAndAcquirerId(code, acquirer.ordinal())).thenReturn(Optional.of(branchPv));

        assertThat(pvService.isValidPv(branchPv), equalTo(false));
    }
}
