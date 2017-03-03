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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.util.Result;

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
     * Test get or create pv when pv does not exists.
     */
    @Test
    public void testGetOrCreatePvWhenPvDoesNotExists() {
        Pv pv = new Pv("code", Acquirer.CIELO);
        when(pvRepository.findByCodeAndAcquirerId(Matchers.any(), Matchers.any())).thenReturn(Optional.empty());
        when(pvRepository.save(pv)).thenReturn(pv);

        assertThat(pvService.getOrCreatePv("code", Acquirer.CIELO), equalTo(pv));
    }

    /**
     * Test get or create pv when pv already exists.
     */
    @Test
    public void testGetOrCreatePvWhenPvAlreadyExists() {
        Pv pv = new Pv("code", Acquirer.CIELO);
        when(pvRepository.findByCodeAndAcquirerId(Matchers.any(), Matchers.any())).thenReturn(Optional.of(pv));

        assertThat(pvService.getOrCreatePv("code", Acquirer.CIELO), equalTo(pv));
        verify(pvRepository, times(0)).save(pv);
    }

    /**
     * Test exists as headquarter when pv does not exist.
     */
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

    /**
     * Test exists as headquarter when pv exists as headquarter.
     */
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

    /**
     * Test exists as headquarter when pv exists as a branch.
     */
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
        MatcherAssert.assertThat(result.failure().get(),
            is("O pv 'code2' já está cadastrado como um pv filial para o adquirente 'CIELO'"));
    }
}
