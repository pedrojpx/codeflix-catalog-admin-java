package com.pedrojpx.codeflix.admin.catalog.domain.validation.handler;

import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Error;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsExceptionValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler aHandler) {
        throw DomainException.with(aHandler.getErrors());
    }

    @Override
    public ValidationHandler handle(Validation aValidation) {
        try {
            aValidation.validate();
        } catch(final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
