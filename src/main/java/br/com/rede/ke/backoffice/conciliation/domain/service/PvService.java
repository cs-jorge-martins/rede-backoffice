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

import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.util.Result;

/**
 * The Class PvService.
 */
@Service
public class PvService {

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

    public Pv getOrCreatePv(String code, Acquirer acquirer) {
        Optional<Pv> pvOpt = repository.findByCodeAndAcquirerId(code, acquirer.ordinal());
        return pvOpt.orElseGet(() -> repository.save(new Pv(code, acquirer)));
    }

    /**
     * Verifies if exists as headquarter.
     * 
     * @return Validation.
     */
    public Validation<Pv> existsAsHeadquarter() {
        return pv -> {
            Optional<Pv> pvOpt = repository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());
            if (pvOpt.isPresent() && !pvOpt.get().isHeadquarter()) {
                return Result
                    .failure(String.format("O pv '%s' já está cadastrado como um pv filial para o adquirente '%s'",
                        pv.getCode(), pv.getAcquirer()));
            }
            return Result.success(pv);
        };
    }

    /**
     * Verifies if exists.
     * 
     * @return Validation.
     */
    public Validation<Pv> exists() {
        return pv -> {
            Optional<Pv> pvOpt = repository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());
            if (!pvOpt.isPresent()) {
                return Result.failure(
                    String.format("O pv '%s' não existe para o adquirente '%s'", pv.getCode(), pv.getAcquirer()));
            }
            return Result.success(pv);
        };
    }

    /**
     * Verifies if exits.
     * 
     * @param pv
     *            pv.
     * @return Result.
     */
    public Result<Pv, String> exists(Pv pv) {
        return exists().validate(pv);
    }

    /**
     * Verifies if exists as headquarter.
     * 
     * @param pv
     *            pv.
     * @return Result.
     */
    public Result<Pv, String> existsAsHeadquarter(Pv pv) {
        return existsAsHeadquarter().validate(pv);
    }
}
