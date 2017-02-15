/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvFormat.java
 * Descrição: PvFormat.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 08/02/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.validation;

/**
 * Class PvFormat.
 */
public class PvFormat {
    private int size;
    private String formatRegex;

    public PvFormat(int size, String formatRegex) {
        this.size = size;
        this.formatRegex = formatRegex;
    }

    public int getSize() {
        return size;
    }

    public String getFormatRegex() {
        return formatRegex;
    }
}
