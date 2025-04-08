package com.pedrojpx.codeflix.admin.catalog.application.category.create;

public record CreateCategoryInput(
        String name,
        String description,
        boolean isActive
) {

    public static CreateCategoryInput with(
        String name,
        String description,
        boolean isActive
    ) {
        return new CreateCategoryInput(name, description, isActive);
    }
}
