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

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;

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
     * @param pvRepository the pv repository
     */
    public PvService(PvRepository pvRepository){
        this.repository = pvRepository;
    }
    
    /**
     * Checks if is valid pv format.
     *
     * @param pv the pv
     * @return true, if is valid pv format
     */
    public boolean isValidPvFormat(Pv pv) {
        return pv.getCode().matches("[0-9]{1,20}");
    }

    public boolean isValidPv(Pv pv) {
        if (!isValidPvFormat(pv)){
            return false;
        }
        Pv resultPv = repository.findByCode(pv.getCode());
        if (resultPv != null && !resultPv.isHeadquarter()){
            return false;
        }
        return true;
    }

    /**
     * Generate pv batch.
     *
     * @param pvs the pvs
     * @return the pv batch
     */
    public PvBatch generatePvBatch(List<Pv> pvs){
        PvBatch pvBatch = new PvBatch();

        for (Pv pv: pvs) {
            if (isValidPv(pv)) {
                pvBatch.addValidPv(pv);
            } else {
                pvBatch.addInvalidPv(pv);
            }
        }
        
        return pvBatch;
    }
}
