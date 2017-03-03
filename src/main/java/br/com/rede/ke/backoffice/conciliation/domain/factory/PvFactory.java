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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.service.FileService;
import br.com.rede.ke.backoffice.conciliation.domain.validation.PvFormat;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.conciliation.exception.InvalidFileException;
import br.com.rede.ke.backoffice.util.Result;

/**
 * A factory for creating Pv objects.
 */
@Component
public class PvFactory {

    private static final Map<Acquirer, PvFormat> PV_FORMAT_ACQUIRER = ImmutableMap.<Acquirer, PvFormat>builder()
        .put(Acquirer.CIELO, new PvFormat(10, "[0-9]{1,10}", "%010d"))
        .put(Acquirer.REDE, new PvFormat(9, "[0-9]{1,9}", "%09d"))
        .build();

    /**
     * From file and acquirer.
     *
     * @param file
     *            the file
     * @param acquirer
     *            the acquirer
     * @return the list
     * @throws InvalidFileException
     *             the invalid file exception
     */
    public List<Result<Pv, String>> fromFileAndAcquirer(MultipartFile file, Acquirer acquirer)
        throws InvalidFileException {
        try {
            return FileService.processFileLineByLine(file).stream()
                .map(validateFormatAndAdjustToAcquirer(acquirer))
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new InvalidFileException("Erro ao processar o arquivo");
        }
    }

    public Result<Pv, String> fromCodeAndAcquirer(String pvCode, Acquirer acquirer) {
        return validatePvCode(acquirer, pvCode);
    }

    protected Function<String, Result<Pv, String>> validateFormatAndAdjustToAcquirer(Acquirer acquirer) {
        return pvCode -> validatePvCode(acquirer, pvCode);
    }

    private Result<Pv, String> validatePvCode(Acquirer acquirer, String pvCode) {
        Result<String, String> result = hasValidSizeAndFormat(pvCode, acquirer);
        if (result.isFailure()) {
            return Result.failure(result.failure().get());
        }
        String codeWithLeftPadding = applyLeftPadding(pvCode, acquirer);
        return Result.success(new Pv(codeWithLeftPadding, acquirer));
    }

    protected Result<String, String> hasValidSizeAndFormat(String pvCode, Acquirer acquirer) {
        PvFormat pvFormat = PV_FORMAT_ACQUIRER.get(acquirer);
        return isValidSize(pvFormat.getSize(), acquirer)
            .and(isValidFormat(pvFormat.getFormatRegex(), acquirer))
            .validate(pvCode);
    }

    protected String applyLeftPadding(String pvCode, Acquirer acquirer) {
        PvFormat pvFormat = PV_FORMAT_ACQUIRER.get(acquirer);
        return String.format(pvFormat.getLeftPaddingRegex(), Integer.parseInt(pvCode));
    }

    protected Validation<String> isValidSize(int size, Acquirer acquirer) {
        return code -> Optional.of(code)
            .filter(code1 -> code1.length() <= size)
            .map(Result::<String, String>success)
            .orElseGet(() -> Result.failure(
                String.format("O pv '%s' está no formato inválido para o adquirente '%s' (entre 1 e %s caracteres)",
                    code, acquirer, size)));
    }

    /**
     * Verifies if the format o the code is valid.
     * 
     * @param formatRegex
     *            regex to validate the format.
     * @return validation.
     */
    protected Validation<String> isValidFormat(String formatRegex, Acquirer acquirer) {
        return code -> Optional.of(code)
            .filter(code1 -> code1.matches(formatRegex))
            .map(Result::<String, String>success)
            .orElseGet(() -> Result.failure(
                String.format("O pv '%s' está no formato inválido para o adquirente '%s' (somente números)", code,
                    acquirer)));
    }
}
