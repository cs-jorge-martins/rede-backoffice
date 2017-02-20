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

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.util.Result;
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

    public Pv getOrCreatePv(String code, Acquirer acquirer) {
        Optional<Pv> pvOpt = repository.findByCodeAndAcquirerId(code, acquirer.ordinal());
        return pvOpt.orElseGet(() -> repository.save(new Pv(code, acquirer)));
    }

    public Validation<Pv> existsAsHeadquarter() {
        return pv -> {
            Optional<Pv> pvOpt = repository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());
            if (pvOpt.isPresent() && !pvOpt.get().isHeadquarter()) {
                return Result.failure(String.format("O pv '%s' já está cadastrado como um pv filial", pv.getCode()));
            }
            return Result.success(pv);
        };
    }

    public Validation<Pv> exists() {
        return pv -> {
            Optional<Pv> pvOpt = repository.findByCodeAndAcquirerId(pv.getCode(), pv.getAcquirerId());
            if (!pvOpt.isPresent()) {
                return Result.failure(String.format("O pv '%s' não existe", pv.getCode()));
            }
            return Result.success(pv);
        };
    }

    public Result<Pv, String> exists(Pv pv) {
        return exists().validate(pv);
    }

    public Result<Pv, String> existsAsHeadquarter(Pv pv) {
        return existsAsHeadquarter().validate(pv);
    }
}
