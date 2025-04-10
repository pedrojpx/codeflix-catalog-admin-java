package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list;

import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway gateway;

    public DefaultListCategoriesUseCase(final CategoryGateway gw) {
        this.gateway = Objects.requireNonNull(gw);
    }

    @Override
    public Pagination<CategoryListItemOutput> execute(CategorySearchQuery input) {
        return this.gateway.findAll(input).map(CategoryListItemOutput::from);
    }
}
