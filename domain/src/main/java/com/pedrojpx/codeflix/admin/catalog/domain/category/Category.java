package com.pedrojpx.codeflix.admin.catalog.domain.category;

import com.pedrojpx.codeflix.admin.catalog.domain.AggregateRoot;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Category extends AggregateRoot<CategoryID> {
    private String name;
    private String description;
    private boolean active;
    //o instant se baseia no tempo a partir do momento da instanciação da variável ao invés to epoch. Para este caso é mais preciso
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID id, final String name, final String description, boolean active,
            final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        return new Category(id, name, description, isActive, now, now, null);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}