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
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.util.Result;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    public void testGetOrCreatePvWhenPvDoesNotExists() {
        Pv pv = new Pv("code", Acquirer.CIELO);
        when(pvRepository.findByCodeAndAcquirerId(Matchers.any(), Matchers.any())).thenReturn(Optional.empty());
        when(pvRepository.save(pv)).thenReturn(pv);

        assertThat(pvService.getOrCreatePv("code", Acquirer.CIELO), equalTo(pv));
    }

    @Test
    public void testGetOrCreatePvWhenPvAlreadyExists() {
        Pv pv = new Pv("code", Acquirer.CIELO);
        when(pvRepository.findByCodeAndAcquirerId(Matchers.any(), Matchers.any())).thenReturn(Optional.of(pv));

        assertThat(pvService.getOrCreatePv("code", Acquirer.CIELO), equalTo(pv));
        verify(pvRepository, times(0)).save(pv);
    }

    @Test
    public void testIsValidSizeWhenSizeIsInvalid() {
        Validation<Pv> validSize = pvService.isValidSize(10);

        Result<Pv, String> result = validSize.validate(new Pv("12345678901"));

        MatcherAssert.assertThat(result.isFailure(), is(true));
        MatcherAssert.assertThat(result.failure().get(), is("O pv '12345678901' está no formato inválido (entre 1 e 10 caracteres)"));
    }

    @Test
    public void testIsValidSizeWhenSizeIsValid() {
        Validation<Pv> validSize = pvService.isValidSize(10);
        Pv pv = new Pv("1234567890");

        Result<Pv, String> result = validSize.validate(pv);

        MatcherAssert.assertThat(result.isSuccess(), is(true));
        MatcherAssert.assertThat(result.success().get(), is(pv));
    }

    @Test
    public void testIsValidFormatWhenFormatDoesNotMatchRegex() {
        Validation<Pv> validFormat = pvService.isValidFormat("[0-9]{1,10}");

        Result<Pv, String> result = validFormat.validate(new Pv("a234567890"));

        MatcherAssert.assertThat(result.isFailure(), is(true));
        MatcherAssert.assertThat(result.failure().get(), is("O pv 'a234567890' está no formato inválido (somente números)"));
    }

    @Test
    public void testIsValidFormatWhenFormatMatchesRegex() {
        Validation<Pv> validFormat = pvService.isValidFormat("[0-9]{1,10}");

        Pv pv = new Pv("12345678");
        Result<Pv, String> result = validFormat.validate(pv);

        MatcherAssert.assertThat(result.isSuccess(), is(true));
        MatcherAssert.assertThat(result.success().get(), is(pv));
    }

    @Test
    public void testExistsAsHeadquarterWhenPvDoesNotExist() {
        when(pvRepository.findByCodeAndAcquirerId("code", Acquirer.CIELO.ordinal()))
            .thenReturn(Optional.empty());

        Validation<Pv> existsAsHeadquarter = this.pvService.existsAsHeadquarter();

        Pv pv = new Pv("code", Acquirer.CIELO);
        Result<Pv, String> result = existsAsHeadquarter.validate(pv);

        MatcherAssert.assertThat(result.isSuccess(), is(true));
        MatcherAssert.assertThat(result.success().get(), is(pv));
    }

    @Test
    public void testExistsAsHeadquarterWhenPvExistsAsHeadquarter() {
        Pv headquarter = new Pv("code", Acquirer.CIELO);
        when(pvRepository.findByCodeAndAcquirerId("code", Acquirer.CIELO.ordinal()))
            .thenReturn(Optional.of(headquarter));

        Validation<Pv> existsAsHeadquarter = this.pvService.existsAsHeadquarter();
        Result<Pv, String> result = existsAsHeadquarter.validate(headquarter);

        MatcherAssert.assertThat(result.isSuccess(), is(true));
        MatcherAssert.assertThat(result.success().get(), is(headquarter));
    }

    @Test
    public void testExistsAsHeadquarterWhenPvExistsAsABranch() {
        Pv headquarter = new Pv("code1", Acquirer.CIELO);
        Pv branch = new Pv("code2", Acquirer.CIELO);
        branch.setHeadquarter(headquarter);

        when(pvRepository.findByCodeAndAcquirerId("code2", Acquirer.CIELO.ordinal()))
            .thenReturn(Optional.of(branch));

        Validation<Pv> existsAsHeadquarter = this.pvService.existsAsHeadquarter();
        Result<Pv, String> result = existsAsHeadquarter.validate(branch);

        MatcherAssert.assertThat(result.isFailure(), is(true));
        MatcherAssert.assertThat(result.failure().get(), is("O pv 'code2' já está cadastrado como um pv filial"));
    }
}
