/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : Validation.java
 * Descrição: Validation.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 07/02/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.validation;

import br.com.rede.ke.backoffice.util.Result;

/**
 * The Validation class.
 * @param <T> type.
 */
@FunctionalInterface
public interface Validation<T> {
    /**
     * Do and in two validations.
     * @param other other validation.
     * @return AndValidation.
     */
    default Validation<T> and(Validation<T> other) {
        return new AndValidation<>(this, other);
    }

    /**
     * Validate object.
     * @param object to be validated.
     * @return Result.
     */
    Result<T, String> validate(T object);

    /**
     * The AndValidation class.
     * @param <T> type.
     */
    class AndValidation<T> implements Validation<T> {
        private final Validation<T> left;
        private final Validation<T> right;

        /**
         * Constructor.
         * @param left left validation.
         * @param right right validation.
         */
        public AndValidation(Validation<T> left, Validation<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Result<T, String> validate(T t) {
            return left.validate(t).flatMap(right::validate);
        }
    }
}
