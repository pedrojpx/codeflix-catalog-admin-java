package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get;

import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.Error;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private final CategoryGateway gateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway gw) {
        this.gateway = gw;
    }

    @Override
    public CategoryOutput execute(String input) {
        final var id = CategoryID.from(input);
        return this.gateway.findById(id)
                .map(CategoryOutput::from)
                .orElseThrow(
                    () -> DomainException.with(new Error("Category id ID %s not found".formatted(id.getValue())))
        );
    }
}
