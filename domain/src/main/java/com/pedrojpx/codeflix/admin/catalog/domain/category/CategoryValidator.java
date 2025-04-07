package com.pedrojpx.codeflix.admin.catalog.domain.category;

import com.pedrojpx.codeflix.admin.catalog.domain.validation.ValidationHandler;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Validator;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Error;

public class CategoryValidator extends Validator {

    private final Category category;

    public CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        String name = this.category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if (length > 255 || length < 3) {
            this.validationHandler().append(new Error("'name' should be between 3 and 255 characters"));
        }
    }
}
