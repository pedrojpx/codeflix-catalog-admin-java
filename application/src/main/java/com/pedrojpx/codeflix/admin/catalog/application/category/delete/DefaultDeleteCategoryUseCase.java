package com.pedrojpx.codeflix.admin.catalog.application.category.delete;

import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{

    private final CategoryGateway gateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(String input) {
        this.gateway.deleteById(CategoryID.from(input));
    }
}
