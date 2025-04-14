package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


@MySqlGatewayTest
public class CategoryMySqlGatewayTest {

    @Autowired
    private CategoryGateway gateway;

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testInjectedDependencies() {
        assertNotNull(gateway);
        assertNotNull(repo);
    }

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var name = "filmes";
        final var description = "desc";
        final var isActive = true;
        final var cat = Category.newCategory(name, description, isActive);

        assertEquals(0, repo.count());

        //this tests what comes through the gateway
        final var found = gateway.save(cat);
        assertEquals(1, repo.count());
        assertEquals(name, found.getName());
        assertEquals(description, found.getDescription());
        assertEquals(isActive, found.isActive());
        assertEquals(cat.getId(), found.getId());
        assertEquals(cat.getCreatedAt(), found.getCreatedAt());
        assertEquals(cat.getUpdatedAt(), found.getUpdatedAt());
        assertNull(found.getDeletedAt());

        //this tests reading directly from the repo (comparing to other projects, these would be in separate tests... repo and gateway)
        var dbEntity = repo.findById(cat.getId().getValue()).get();
        assertEquals(1, repo.count());
        assertEquals(name, dbEntity.getName());
        assertEquals(description, dbEntity.getDescription());
        assertEquals(isActive, dbEntity.isActive());
        assertEquals(cat.getId().getValue(), dbEntity.getId());
        assertEquals(cat.getCreatedAt(), dbEntity.getCreatedAt());
        assertEquals(cat.getUpdatedAt(), dbEntity.getUpdatedAt());
        assertNull(dbEntity.getDeletedAt());

    }
}
