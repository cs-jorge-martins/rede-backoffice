/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvService.java
 * Descrição: PvService.java.
 * Autor    : Karlisson Bezerra <kbezerra@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;


/**
 * Class StringToPvPermissionIdConverter.
 */
@Component
public class StringToPvPermissionIdConverter implements Converter<String, PvPermissionId> {

    /**
     * Convert.
     * @param source source.
     * @return PvPermissionId.
     */
    @Override
    public PvPermissionId convert(String source) {
        String[] ids = source.split("-");
        return new PvPermissionId(Long.parseLong(ids[0]), Long.parseLong(ids[1]));
    }
}