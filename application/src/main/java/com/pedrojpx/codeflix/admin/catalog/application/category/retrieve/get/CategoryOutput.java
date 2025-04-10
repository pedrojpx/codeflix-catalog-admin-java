package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt

) {
    public static CategoryOutput from(final Category c) {
        return new CategoryOutput(
                c.getId(),
                c.getName(),
                c.getDescription(),
                c.isActive(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getDeletedAt()
        );
    }
}
