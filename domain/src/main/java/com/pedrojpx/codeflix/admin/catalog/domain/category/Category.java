package com.pedrojpx.codeflix.admin.catalog.domain.category;

import com.pedrojpx.codeflix.admin.catalog.domain.AggregateRoot;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Category extends AggregateRoot<CategoryID> implements Cloneable {
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
        return new Category(id, name, description, isActive, now, now, isActive ? null:now);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String aName, final String aDescription, boolean isActive) {
        this.name = aName;
        this.description = aDescription;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.updatedAt = Instant.now();
        return this;
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

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}