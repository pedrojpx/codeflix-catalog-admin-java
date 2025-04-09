package com.pedrojpx.codeflix.admin.catalog.application.category.update;

public record UpdateCategoryInput(
        String id,
        String name,
        String description,
        boolean isActive
) {
    public static UpdateCategoryInput with(
        String id,
        String name,
        String description,
        boolean isActive
    ) {
        return new UpdateCategoryInput(id, name, description, isActive);
    }
}
