/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : ResultTest.java
 * Descrição: ResultTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 10/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * The ResultTest class.
 */
public class ResultTest {

    /**
     * Test is success.
     */
    @Test
    public void testIsSuccess() {
        assertThat(Result.success(null).isSuccess(), equalTo(true));
        assertThat(Result.failure(null).isSuccess(), equalTo(false));
    }

    /**
     * Test is failure.
     */
    @Test
    public void testIsFailure() {
        assertThat(Result.failure(null).isFailure(), equalTo(true));
        assertThat(Result.success(null).isFailure(), equalTo(false));
    }

    /**
     * Test get success optional from success result.
     */
    @Test
    public void testGetSuccessOptionalFromSuccessResult() {
        Result<Integer, Integer> success = Result.success(1);
        Optional<Integer> value = success.success();

        assertThat(value.isPresent(), equalTo(true));
        assertThat(value.get(), equalTo(1));
    }

    /**
     * Test get success optional from failure result.
     */
    @Test
    public void testGetSuccessOptionalFromFailureResult() {
        Result<Integer, Integer> failure = Result.failure(1);
        Optional<Integer> value = failure.success();

        assertThat(value.isPresent(), equalTo(false));
    }

    /**
     * Test get failure optional from failure result.
     */
    @Test
    public void testGetFailureOptionalFromFailureResult() {
        Result<Integer, Integer> failure = Result.failure(1);
        Optional<Integer> value = failure.failure();

        assertThat(value.isPresent(), equalTo(true));
        assertThat(value.get(), equalTo(1));
    }

    /**
     * Test get failure optional from success result.
     */
    @Test
    public void testGetFailureOptionalFromSuccessResult() {
        Result<Integer, Integer> success = Result.success(1);
        Optional<Integer> value = success.failure();

        assertThat(value.isPresent(), equalTo(false));
    }

    /**
     * Test map with success result.
     */
    @Test
    public void testMapWithSuccessResult() {
        Result<Integer, Integer> success = Result.success(1);
        Result<String, Integer> result = success.map(String::valueOf);

        assertThat(result instanceof Result.Success, equalTo(true));
        assertThat(((Result.Success<String, Integer>) result).getValue(), equalTo("1"));
    }

    /**
     * Test map with failure result.
     */
    @Test
    public void testMapWithFailureResult() {
        Result<Integer, Integer> failure = Result.failure(1);
        Result<String, Integer> result = failure.map(String::valueOf);

        assertThat(result instanceof Result.Failure, equalTo(true));
        assertThat(((Result.Failure<String, Integer>) result).getValue(), equalTo(1));
    }

    /**
     * test flatMap success result with success result mapper.
     */
    @Test
    public void testFlatMapSuccessResultWithSuccessResultMapper() {
        Result<Integer, Integer> success = Result.success(1);
        Result<Integer, Integer> result = success.flatMap(x -> Result.success(x + x));

        assertThat(result instanceof Result.Success, equalTo(true));
        assertThat(((Result.Success<Integer, Integer>) result).getValue(), equalTo(2));
    }

    /**
     * Test flatMap success result with failure result mapper.
     */
    @Test
    public void testFlatMapSuccessResultWithFailureResultMapper() {
        Result<Integer, Integer> success = Result.success(1);
        Result<Integer, Integer> result = success.flatMap(x -> Result.failure(2));

        assertThat(result instanceof Result.Failure, equalTo(true));
        assertThat(((Result.Failure<Integer, Integer>) result).getValue(), equalTo(2));
    }

    /**
     * Test flatMap failure result with success result mapper.
     */
    @Test
    public void testFlatMapFailureResultWithSuccessResultMapper() {
        Result<Integer, Integer> failure = Result.failure(1);
        Result<Integer, Integer> result = failure.flatMap(x -> Result.success(x + x));

        assertThat(result instanceof Result.Failure, equalTo(true));
        assertThat(((Result.Failure<Integer, Integer>) result).getValue(), equalTo(1));
    }

    /**
     * Test flatMap failure result with failure result mapper.
     */
    @Test
    public void testFlatMapFailureResultWithFailureResultMapper() {
        Result<Integer, Integer> failure = Result.failure(1);
        Result<Integer, Integer> result = failure.flatMap(x -> Result.failure(2));

        assertThat(result instanceof Result.Failure, equalTo(true));
        assertThat(((Result.Failure<Integer, Integer>) result).getValue(), equalTo(1));
    }

    /**
     * Test get success values.
     */
    @Test
    public void testGetSuccessValues() {
        List<Result<String, Integer>> results = Arrays.asList(Result.failure(1), Result.success("OK"),
            Result.failure(3));

        List<String> successValues = Result.getSuccessValues(results);
        assertThat(successValues, equalTo(Arrays.asList("OK")));
    }

    /**
     * Test get failure values.
     */
    @Test
    public void testGetFailureValues() {
        List<Result<String, Integer>> results = Arrays.asList(Result.failure(1), Result.failure(2),
            Result.success("Teste"));

        List<Integer> failureValues = Result.getFailureValues(results);
        assertThat(failureValues, equalTo(Arrays.asList(1, 2)));
    }
}