package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListItemOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static CategoryListItemOutput from(final Category c) {
        return new CategoryListItemOutput(
                c.getId(),c.getName(),c.getDescription(),c.isActive(),c.getCreatedAt(),c.getUpdatedAt(),c.getDeletedAt()
        );
    }
}
