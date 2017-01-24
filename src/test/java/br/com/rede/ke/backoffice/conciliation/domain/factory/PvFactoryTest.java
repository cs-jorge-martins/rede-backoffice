/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvFactoryTest.java
 * Descrição: PvFactoryTest.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 19/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.exception.InvalidFileException;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The PvFactoryTest Class.
 */
public class PvFactoryTest {

    /**
     * Test from file and acquirer when reads from file.
     * @throws IOException when an error occurs.
     */
    @Test
    public void testFromFileAndAcquirerWhenReadsFromFile() throws IOException {
        String pvs = "1000201314\n1014766629\n1000201330\n1005867493\n";
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(pvs.getBytes()));

        List<Pv> pvList = PvFactory.fromFileAndAcquirer(multipartFile, Acquirer.CIELO);

        assertThat(pvList.size(), equalTo(4));
    }

    /**
     * Test from file and acquirer when problem reading file.
     * @throws IOException when an error occurs.
     */
    @Test(expected = InvalidFileException.class)
    public void testFromFileAndAcquirerWhenProblemReadingFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        PvFactory.fromFileAndAcquirer(multipartFile, Acquirer.CIELO);
    }
}