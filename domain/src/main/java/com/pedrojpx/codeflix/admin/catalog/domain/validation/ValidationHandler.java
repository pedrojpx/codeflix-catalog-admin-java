package com.pedrojpx.codeflix.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler aHandler);

    ValidationHandler handle(Validation aValidation);

    public List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !(getErrors().isEmpty());
    }

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        } else {
            return null;
        }
    }

    public interface Validation {
        void validate();
    }
}
