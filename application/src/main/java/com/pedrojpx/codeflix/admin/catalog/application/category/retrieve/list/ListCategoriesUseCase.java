package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list;

import com.pedrojpx.codeflix.admin.catalog.application.UseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListItemOutput>> {
}
