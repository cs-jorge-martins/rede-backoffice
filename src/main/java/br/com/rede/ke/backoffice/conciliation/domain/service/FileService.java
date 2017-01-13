/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : FileService.java
 * Descrição: FileService.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

/**
 * Class FileService.
 */
public final class FileService {

    private FileService() {
        // empty
    }

    /**
     * Read a multipart file line by line collecting each line to a list.
     * @param file multipart file.
     * @return List in which each item is a line of file
     * @throws IOException in case of any problem reading a file
     */
    public static List<String> processFileLineByLine(MultipartFile file) throws IOException {
        try(BufferedReader fileBufferReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return fileBufferReader.lines().collect(Collectors.toList());
        }
    }
}
