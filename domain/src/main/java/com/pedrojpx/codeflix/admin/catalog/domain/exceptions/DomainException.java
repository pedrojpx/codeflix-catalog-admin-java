package com.pedrojpx.codeflix.admin.catalog.domain.exceptions;

import java.util.List;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Error;

public class DomainException extends NoStackTraceException {

    private final List<Error> errors;
    private DomainException(final String aMessage, final List<Error> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public static DomainException with(final Error error) {
        return new DomainException("", List.of(error));
    }



    public List<Error> getErrors() {
        return errors;
    }
}
