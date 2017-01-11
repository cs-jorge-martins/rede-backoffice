/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvRepositoryIT.java
 * Descrição: PvRepositoryIT.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 11/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.domain.repository;

import br.com.rede.ke.backoffice.Application;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Class PvRepositoryIT.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PvRepositoryIT {

    @Autowired
    private PvRepository pvRepository;

    @Transactional
    @Test
    public void savePvWhenNoPvWithSameCodeAndAcquirerExists() throws Exception {
        Pv pv = new Pv();
        pv.setCode("12300000");
        pv.setAcquirerId(Acquirer.CIELO.ordinal());

        Pv savedPv = pvRepository.save(pv);
        assertThat(savedPv.getAcquirer(), equalTo(Acquirer.CIELO));
        assertThat(savedPv.getCode(), equalTo("12300000"));
    }

    @Transactional
    @Test
    public void savePvWhenExistsPvWithSameCodeDifferentAcquirer() throws Exception {
        Pv pv = new Pv();
        pv.setCode("12300000");
        pv.setAcquirerId(Acquirer.CIELO.ordinal());

        pvRepository.save(pv);

        Pv pvWithSameCodeAndDifferentAcquirer = new Pv();
        pvWithSameCodeAndDifferentAcquirer.setCode("12300000");
        pvWithSameCodeAndDifferentAcquirer.setAcquirerId(Acquirer.REDE.ordinal());

        Pv savedPv = pvRepository.save(pvWithSameCodeAndDifferentAcquirer);
        assertThat(savedPv.getAcquirer(), equalTo(Acquirer.REDE));
        assertThat(savedPv.getCode(), equalTo("12300000"));
    }

    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void doNotSavePvWhenExistsPvWithSameCodeAndAcquirer() throws Exception {
        Pv pv = new Pv();
        pv.setCode("12300000");
        pv.setAcquirerId(Acquirer.CIELO.ordinal());

        pvRepository.save(pv);

        Pv pvWithSameCodeAndAcquirer = new Pv();
        pvWithSameCodeAndAcquirer.setCode("12300000");
        pvWithSameCodeAndAcquirer.setAcquirerId(Acquirer.CIELO.ordinal());

        pvRepository.save(pvWithSameCodeAndAcquirer);
    }

}
