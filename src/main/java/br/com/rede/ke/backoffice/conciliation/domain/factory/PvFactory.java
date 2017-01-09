/*
 * Copyright 2016 Rede S.A.
 *************************************************************
 * Nome     : PvFactory.java
 * Descrição: PvFactory
 * Autor    : Karlisson Bezerra <kbezerra@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : Thoughtworks
 */

package br.com.rede.ke.backoffice.conciliation.domain.factory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;

/**
 * A factory for creating Pv objects.
 */
public final class PvFactory {
    
    /**
     * Instantiates a new pv factory.
     */
    private PvFactory(){
    }
    
    /**
     * From string.
     *
     * @param pvList the pv list
     * @return the list
     */
    public static List<Pv> fromCodesAndAcquirer(String pvList, Acquirer acquirer) {
        return Arrays.asList(pvList.split("\n")).stream()
               .map(code -> new Pv(code, acquirer))
               .collect(Collectors.toList());
    }
}
