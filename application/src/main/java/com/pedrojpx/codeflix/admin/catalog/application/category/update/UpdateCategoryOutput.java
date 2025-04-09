package com.pedrojpx.codeflix.admin.catalog.application.category.update;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id
) {
    public static UpdateCategoryOutput from(final Category c) {
        return new UpdateCategoryOutput(c.getId());
    }
}
