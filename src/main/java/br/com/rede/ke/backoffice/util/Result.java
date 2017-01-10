/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : Result.java
 * Descrição: Result.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 10/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * Result<S, F> is the type used for returning and propagating errors
 *
 * Result can assume two types that represents either {@link Success} and {@link Failure}.
 * @param <S> type of success object.
 * @param <F> type of failure object.
 */
public interface Result<S, F> {

    /**
     * Check if is success result.
     * @return if is success result
     */
    boolean isSuccess();

    /**
     * Check if is failure result.
     * @return if is failure result
     */
    boolean isFailure();

    /**
     * Return on optional of success value.
     * @return An optional of success value
     */
    Optional<S> success();

    /**
     * Return on optional of failure value.
     * @return An optional of failure value
     */
    Optional<F> failure();

    /**
     * Map new result.
     *
     * @see java.util.Optional#map(Function)
     * @param mapper the mapper function (Functor)
     * @param <U> success mapped type
     * @return new mapped Result
     */
    <U> Result<U, F> map(Function<S, U> mapper);

    /**
     * Combine two results
     *
     * @see java.util.Optional#flatMap(Function)
     * @param mapper the mapper function (Monad)
     * @param <U> success mapped type
     * @return new mapped Result
     */
    <U> Result<U, F> flatMap(Function<S, Result<U, F>> mapper);

    /**
     * Create new success result.
     * @param value the success value
     * @param <S> the success object type
     * @param <F> the failure object type
     * @return new success result
     */
    static <S, F> Success<S, F> success(S value) {
        return new Success<>(value);
    }

    /**
     * Create new failure result.
     * @param value the failure value
     * @param <S> the success object type
     * @param <F> the failure object type
     * @return new failure result
     */
    static <S, F> Failure<S, F> failure(F value) {
        return new Failure<>(value);
    }

    /**
     * The Success<S, F> represents a successful computation.
     * @param <S> the success object type
     * @param <F> the failure object type
     */
    class Success<S, F> implements Result<S, F> {
        /**
         * the success value.
         */
        private final S value;

        /**
         * The success constructor.
         * @param value the success value
         */
        public Success(S value) {
            this.value = value;
        }

        /**
         * Get success value.
         * @return success value
         */
        public S getValue() {
            return this.value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public Optional<S> success() {
            return Optional.of(this.value);
        }

        @Override
        public Optional<F> failure() {
            return Optional.empty();
        }

        @Override
        public <U> Result<U, F> map(Function<S, U> mapper) {
            return Result.success(mapper.apply(this.value));
        }

        @Override
        public <U> Result<U, F> flatMap(Function<S, Result<U, F>> mapper) {
            return mapper.apply(this.value);
        }
    }

    /**
     * The Failure<S, F> represents a failed computation.
     * @param <S> the success object type
     * @param <F> the failure object type
     */
    class Failure<S, F> implements Result<S, F> {
        /**
         * the failure value
         */
        private final F value;

        /**
         * The Failure constructor.
         * @param value the failure value
         */
        public Failure(F value) {
            this.value = value;
        }

        /**
         * Get the failure value.
         * @return the failure value
         */
        public F getValue() {
            return this.value;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public Optional<S> success() {
            return Optional.empty();
        }

        @Override
        public Optional<F> failure() {
            return Optional.of(this.value);
        }

        @Override
        public <U> Result<U, F> map(Function<S, U> mapper) {
            return Result.failure(this.value);
        }

        @Override
        public <U> Result<U, F> flatMap(Function<S, Result<U, F>> mapper) {
            return Result.failure(this.value);
        }
    }
}
