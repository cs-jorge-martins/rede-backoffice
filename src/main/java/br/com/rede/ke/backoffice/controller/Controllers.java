/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : ControllersUtil.java
 * Descrição: ControllersUtil.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;

/**
 * Class Controllers.
 */
public final class Controllers {

    private Controllers() {
        //empty
    }

    /**
     * Acquirers without rede.
     *
     * @return the list
     */
    public static List<Acquirer> acquirersWithoutRede() {
        return Arrays.stream(Acquirer.values())
            .filter(a -> !(Acquirer.REDE.equals(a) || Acquirer.NULL.equals(a)))
            .collect(Collectors.toList());
    }
}
