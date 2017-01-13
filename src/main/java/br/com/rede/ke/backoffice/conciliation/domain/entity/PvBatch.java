/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvBatch.java
 * Descrição: PvBatch.java.
 * Autor    : Karlisson Bezerra <kbezerra@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * The Class PvBatch.
 */
public class PvBatch {
    
    /** The successful pvs. */
    private List<Pv> validPvs;
    
    /** The failed pvs. */
    private List<Pv> invalidPvs;
    
    public PvBatch(){
        this.validPvs = new ArrayList<>();
        this.invalidPvs = new ArrayList<>();
    }
    
    /**
     * Gets the successful pvs.
     *
     * @return the successful pvs
     */
    public List<Pv> getValidPvs() {
        return ImmutableList.copyOf(validPvs);
    }
    
    /**
     * Adds the successful pv.
     *
     * @param pv the pv
     */
    public void addValidPv(Pv pv) {
        this.validPvs.add(pv);
    }
    
    /**
     * Gets the failed pvs.
     *
     * @return the failed pvs
     */
    public List<Pv> getInvalidPvs() {
        return ImmutableList.copyOf(invalidPvs);
    }
    
    /**
     * Adds the failed pv.
     *
     * @param pv the pv
     */
    public void addInvalidPv(Pv pv) {
        this.invalidPvs.add(pv);
    }
}
