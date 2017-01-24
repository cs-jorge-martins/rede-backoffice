/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvService.java
 * Descrição: PvService.java.
 * Autor    : Karlisson Bezerra <kbezerra@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.Optional;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import org.springframework.stereotype.Service;

/**
 * The Class PvService.
 */
@Service
public class PvService {

    /** pv format regex validation. */
    private static final String PV_FORMAT_REGEX_VALIDATION = "[0-9]{1,20}";

    /** The repository. */
    private PvRepository repository;

    /**
     * Instantiates a new pv service.
     *
     * @param pvRepository
     *            the pv repository
     */
    public PvService(PvRepository pvRepository) {
        this.repository = pvRepository;
    }

    /**
     * Checks if is valid pv format.
     *
     * @param pv
     *            the pv
     * @return true, if is valid pv format
     */
    public boolean isValidPvFormat(Pv pv) {
        return pv.getCode().matches(PV_FORMAT_REGEX_VALIDATION);
    }

    /**
     * Checks if is valid pv.
     *
     * @param pv
     *            the pv
     * @return true, if is valid pv
     */
    public boolean isValidPv(Pv pv) {
        if (!isValidPvFormat(pv)) {
            return false;
        }
        Optional<Pv> resultPv = repository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());
        return resultPv.map(Pv::isHeadquarter).orElse(true);
    }
}
