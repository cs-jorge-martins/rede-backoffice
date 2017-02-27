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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.conciliation.exception.InvalidFileException;
import br.com.rede.ke.backoffice.util.Result;

/**
 * The PvFactoryTest Class.
 */
public class PvFactoryTest {

    private PvFactory pvFactory;

    @Before
    public void setUp() throws Exception {
        this.pvFactory = new PvFactory();
    }

    /**
     * Test from file and acquirer when reads from file.
     *
     * @throws IOException
     *             when an error occurs.
     */
    @Test
    public void testFromFileAndAcquirerWhenReadsFromFile() throws IOException {
        String pvs = "1000201314\n1014766629\n1000201330\n1005867493\n";
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(pvs.getBytes()));

        List<Result<Pv, String>> pvList = pvFactory.fromFileAndAcquirer(multipartFile, Acquirer.CIELO);

        assertThat(pvList.size(), equalTo(4));
    }

    /**
     * Test from file and acquirer when problem reading file.
     *
     * @throws IOException
     *             when an error occurs.
     */
    @Test(expected = InvalidFileException.class)
    public void testFromFileAndAcquirerWhenProblemReadingFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(new IOException());

        pvFactory.fromFileAndAcquirer(multipartFile, Acquirer.CIELO);
    }

    /**
     * Test validate format and adjust to acquirer when pv code has error.
     */
    @Test
    public void testValidateFormatAndAdjustToAcquirerWhenPvCodeHasError() {
        String invalidCode = "a1234556";
        Result<Pv, String> result = pvFactory.validateFormatAndAdjustToAcquirer(Acquirer.CIELO).apply(invalidCode);

        assertThat(result.isFailure(), equalTo(true));
    }

    /**
     * Test validate format and adjust to acquirer when pv code has error.
     */
    @Test
    public void testValidateFormatAndAdjustToAcquirerWhenPvCodeIsOk() {
        String invalidCode = "0123456789";
        Result<Pv, String> result = pvFactory.validateFormatAndAdjustToAcquirer(Acquirer.CIELO).apply(invalidCode);

        assertThat(result.isSuccess(), equalTo(true));
    }

    /**
     * Test has valid size and format when pv code has validation error.
     */
    @Test
    public void testHasValidSizeAndFormatWhenPvCodeHasValidationErrorForCielo() {
        String pvCodeWithWrongSize = "12345678901";
        Result<String, String> result = pvFactory.hasValidSizeAndFormat(pvCodeWithWrongSize, Acquirer.CIELO);

        assertThat(result.isFailure(), equalTo(true));
    }

    /**
     * Test has valid size and format when pv code has validation error.
     */
    @Test
    public void testHasValidSizeAndFormatWhenPvCodeHasValidationErrorForRede() {
        String pvCodeWithWrongSize = "1234567890";
        Result<String, String> result = pvFactory.hasValidSizeAndFormat(pvCodeWithWrongSize, Acquirer.REDE);

        assertThat(result.isFailure(), equalTo(true));
    }

    /**
     * Test has valid size and format when pv code is ok.
     */
    @Test
    public void testHasValidSizeAndFormatWhenPvCodeIsOkForCielo() {
        String pvCodeWithRightSize = "1234567890";
        Result<String, String> result = pvFactory.hasValidSizeAndFormat(pvCodeWithRightSize, Acquirer.CIELO);

        assertThat(result.isSuccess(), equalTo(true));
    }

    /**
     * Test has valid size and format when pv code is ok.
     */
    @Test
    public void testHasValidSizeAndFormatWhenPvCodeIsOkForRede() {
        String pvCodeWithRightSize = "123456789";
        Result<String, String> result = pvFactory.hasValidSizeAndFormat(pvCodeWithRightSize, Acquirer.REDE);

        assertThat(result.isSuccess(), equalTo(true));
    }

    /**
     * Test apply left padding when code less than size ten.
     */
    @Test
    public void testApplyLeftPaddingWhenCodeLessThanSizeTenForCielo() {
        String leftPaddingCode = pvFactory.applyLeftPadding("12345678", Acquirer.CIELO);

        assertThat(leftPaddingCode, equalTo("0012345678"));
    }

    /**
     * Test apply left padding when code less than size ten.
     */
    @Test
    public void testApplyLeftPaddingWhenCodeLessThanSizeNineForRede() {
        String leftPaddingCode = pvFactory.applyLeftPadding("12345678", Acquirer.REDE);

        assertThat(leftPaddingCode, equalTo("012345678"));
    }

    /**
     * Test apply left padding when code has size ten.
     */
    @Test
    public void testApplyLeftPaddingWhenCodeHasSizeTenForCielo() {
        String leftPaddingCode = pvFactory.applyLeftPadding("1234567890", Acquirer.CIELO);

        assertThat(leftPaddingCode, equalTo("1234567890"));
    }

    /**
     * Test apply left padding when code has size ten.
     */
    @Test
    public void testApplyLeftPaddingWhenCodeHasSizeNineForCielo() {
        String leftPaddingCode = pvFactory.applyLeftPadding("123456789", Acquirer.REDE);

        assertThat(leftPaddingCode, equalTo("123456789"));
    }

    /**
     * Test is valid size when size is invalid.
     */
    @Test
    public void testIsValidSizeWhenSizeIsInvalid() {
        Validation<String> validSize = pvFactory.isValidSize(10, Acquirer.CIELO);

        Result<String, String> result = validSize.validate("12345678901");

        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(),
            equalTo("O pv '12345678901' está no formato inválido para o adquirente 'CIELO' (entre 1 e 10 caracteres)"));
    }

    /**
     * Test is valid size when size is valid.
     */
    @Test
    public void testIsValidSizeWhenSizeIsValid() {
        Validation<String> validSize = pvFactory.isValidSize(10, Acquirer.CIELO);
        Result<String, String> result = validSize.validate("1234567890");

        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.success().get(), equalTo("1234567890"));
    }

    /**
     * Test is valid format when format does not match regex.
     */
    @Test
    public void testIsValidFormatWhenFormatDoesNotMatchRegex() {
        Validation<String> validFormat = pvFactory.isValidFormat("[0-9]{1,10}", Acquirer.CIELO);

        Result<String, String> result = validFormat.validate("a234567890");

        assertThat(result.isFailure(), equalTo(true));
        assertThat(result.failure().get(),
            equalTo("O pv 'a234567890' está no formato inválido para o adquirente 'CIELO' (somente números)"));
    }

    /**
     * Test is valid format when format matches regex.
     */
    @Test
    public void testIsValidFormatWhenFormatMatchesRegex() {
        Validation<String> validFormat = pvFactory.isValidFormat("[0-9]{1,10}", Acquirer.CIELO);
        Result<String, String> result = validFormat.validate("12345678");

        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.success().get(), equalTo("12345678"));
    }
}