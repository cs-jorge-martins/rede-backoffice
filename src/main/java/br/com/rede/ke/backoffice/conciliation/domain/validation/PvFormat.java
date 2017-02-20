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
    private String leftPaddingRegex;

    public PvFormat(int size, String formatRegex, String leftPaddingRegex) {
        this.size = size;
        this.formatRegex = formatRegex;
        this.leftPaddingRegex = leftPaddingRegex;
    }

    public int getSize() {
        return size;
    }

    public String getFormatRegex() {
        return formatRegex;
    }

    public String getLeftPaddingRegex() {
        return leftPaddingRegex;
    }
}
