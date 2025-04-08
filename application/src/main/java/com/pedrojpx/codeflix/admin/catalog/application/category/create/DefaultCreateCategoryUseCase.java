package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryInput input) {
        final var cat = Category.newCategory(input.name(), input.description(), input.isActive());
        cat.validate(new ThrowsValidationHandler());

        this.gateway.create(cat);

        return CreateCategoryOutput.from(cat);
    }
}
