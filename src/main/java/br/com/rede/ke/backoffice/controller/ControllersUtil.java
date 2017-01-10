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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.service.FileService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Class ControllersUtil.
 */
public class ControllersUtil {

    /**
     * Acquirers without rede.
     *
     * @return the list
     */
    public static List<Acquirer> acquirersWithoutRede() {
        return Arrays.asList(Acquirer.values())
            .stream()
            .filter(a -> !Acquirer.REDE.equals(a))
            .collect(Collectors.toList());
    }

    public static List<Pv> fromCodesToPvs(MultipartFile file, Acquirer acquirer) throws IOException {
        return FileService.processFileLineByLine(file).stream()
            .map(line -> new Pv(line, acquirer))
            .collect(Collectors.toList());
    }
}
