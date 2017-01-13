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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.service.FileService;
import org.springframework.web.multipart.MultipartFile;

/**
 * A factory for creating Pv objects.
 */
public final class PvFactory {
    
    /**
     * Instantiates a new pv factory.
     */
    private PvFactory(){
        // empty
    }

    /**
     *
     * @param file
     * @param acquirer
     * @return
     * @throws IOException
     */
    public static List<Pv> fromFileAndAcquirer(MultipartFile file, Acquirer acquirer) throws IOException {
        return FileService.processFileLineByLine(file).stream()
            .map(line -> new Pv(line, acquirer))
            .collect(Collectors.toList());
    }
}
