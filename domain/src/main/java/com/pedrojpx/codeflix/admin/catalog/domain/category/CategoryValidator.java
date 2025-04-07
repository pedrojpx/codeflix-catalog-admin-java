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
        if (this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
