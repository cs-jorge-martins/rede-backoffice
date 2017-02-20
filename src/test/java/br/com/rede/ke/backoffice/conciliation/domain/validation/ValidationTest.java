/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : ValidationTest.java
 * Descrição: ValidationTest.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 13/02/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.validation;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.util.Result;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * The ValidationTest class.
 */
public class ValidationTest {
    private Validation<Pv> validResult;
    private Validation<Pv> invalidResult;
    private final String message = "This is invalid";
    private final Pv successPv = new Pv();

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        validResult = (pv) -> Result.success(successPv);
        invalidResult = (pv) -> Result.failure(message);
    }

    /**
     * Test and conjunction valid followed by invalid.
     */
    @Test
    public void testAndConjunctionValidFollowedByInvalid() {
        Validation<Pv> conjunction = validResult.and(invalidResult);
        Result<Pv, String> result = conjunction.validate(new Pv());

        assertThat(result.isFailure(), is(true));
        assertThat(result.failure().get(), is(message));
    }

    /**
     * Test and conjunction invalid followed by valid.
     */
    @Test
    public void testAndConjunctionInvalidFollowedByValid() {
        Validation<Pv> conjunction = invalidResult.and(validResult);
        Result<Pv, String> result = conjunction.validate(new Pv());

        assertThat(result.isFailure(), is(true));
        assertThat(result.failure().get(), is(message));
    }

    /**
     * Test and conjunction valid validations.
     */
    @Test
    public void testAndConjunctionValidValidations() {
        Validation<Pv> conjunction = validResult.and(validResult);
        Result<Pv, String> result = conjunction.validate(new Pv());

        assertThat(result.isSuccess(), is(true));
        assertThat(result.success().get(), is(successPv));
    }

    /**
     * Test and conjunction valid validations.
     */
    @Test
    public void testAndConjunctionInvalidValidations() {
        final String anotherMessage = "This is also invalid";
        Validation<Pv> anotherInvalid = (pv) -> Result.failure(anotherMessage);

        Validation<Pv> conjunction = anotherInvalid.and(invalidResult);
        Result<Pv, String> result = conjunction.validate(new Pv());

        assertThat(result.isFailure(), is(true));
        assertThat(result.failure().get(), is(anotherMessage));
    }
}