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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.service.FileService;
import br.com.rede.ke.backoffice.conciliation.domain.validation.PvFormat;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.conciliation.exception.InvalidFileException;
import br.com.rede.ke.backoffice.util.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * A factory for creating Pv objects.
 */
@Component
public class PvFactory {

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

    protected Function<String, Result<Pv, String>> validateFormatAndAdjustToAcquirer(Acquirer acquirer) {
        return pvCode -> {
            Result<String, String> result = hasValidSizeAndFormat(pvCode);
            if (result.isFailure()) {
                return Result.failure(result.failure().get());
            }
            String codeWithLeftPadding = applyLeftPadding(pvCode);
            return Result.success(new Pv(codeWithLeftPadding, acquirer));
        };
    }

    protected Result<String, String> hasValidSizeAndFormat(String pvCode) {
        PvFormat pvFormat = new PvFormat(10, "[0-9]{1,10}", "%010d");
        return  isValidSize(pvFormat.getSize())
            .and(isValidFormat(pvFormat.getFormatRegex()))
            .validate(pvCode);
    }

    protected String applyLeftPadding(String pvCode) {
        PvFormat pvFormat = new PvFormat(10, "[0-9]{1,10}", "%010d");
        return String.format(pvFormat.getLeftPaddingRegex(), Integer.parseInt(pvCode));
    }

    protected Validation<String> isValidSize(int size) {
        return code -> Optional.of(code)
            .filter(code1 -> code1.length() <= size)
            .map(Result::<String, String>success)
            .orElseGet(()-> Result.failure(
                String.format("O pv '%s' está no formato inválido (entre 1 e %s caracteres)", code, size)));
    }

    /**
     * Verifies if the format o the code is valid.
     * @param formatRegex regex to validate the format.
     * @return validation.
     */
    protected Validation<String> isValidFormat(String formatRegex) {
        return code -> Optional.of(code)
            .filter(code1 -> code1.matches(formatRegex))
            .map(Result::<String, String>success)
            .orElseGet(()-> Result.failure(
                String.format("O pv '%s' está no formato inválido (somente números)", code)));
    }
}
