package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {
    public static CreateCategoryOutput from(final Category c) {
        return new CreateCategoryOutput(c.getId());
    }
}
