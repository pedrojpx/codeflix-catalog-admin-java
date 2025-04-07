package com.pedrojpx.codeflix.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler aHandler);

    ValidationHandler validate(Validation aValidation);

    public List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !(getErrors().isEmpty());
    }

    public interface Validation {
        void validate();
    }
}
