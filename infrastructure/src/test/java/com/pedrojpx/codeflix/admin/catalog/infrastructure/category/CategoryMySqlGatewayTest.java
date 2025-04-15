package com.pedrojpx.codeflix.admin.catalog.infrastructure.category;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnUpdatedCategory() {
        final var name = "filmes";
        final var description = "desc";
        final var isActive = true;
        final var preUpdateCat = Category.newCategory("nome antigo", null, isActive);

        assertEquals(0, repo.count()); //repo is empty

        final var found = repo.saveAndFlush(CategoryJpaEntity.from(preUpdateCat));
        var oldDbEntity = repo.findById(preUpdateCat.getId().getValue()).get();
        //asserts initial value is saved correctly
        assertEquals(1, repo.count());
        assertEquals(preUpdateCat.getId().getValue(), oldDbEntity.getId());
        assertEquals(preUpdateCat.getName(), oldDbEntity.getName());

        final var updateValues = preUpdateCat.clone().update(name,description,isActive);
        final var postUpdateCat = gateway.update(updateValues);
        assertEquals(1, repo.count()); //no new entries created

        //this tests what comes through the gateway
        assertEquals(name, postUpdateCat.getName());
        assertEquals(description, postUpdateCat.getDescription());
        assertEquals(isActive, postUpdateCat.isActive());
        assertEquals(preUpdateCat.getId(), postUpdateCat.getId());
        assertEquals(preUpdateCat.getCreatedAt(), postUpdateCat.getCreatedAt());
        assertTrue(preUpdateCat.getUpdatedAt().isBefore(postUpdateCat.getUpdatedAt()));
        assertNull(postUpdateCat.getDeletedAt());

        //this tests reading directly from the repo (comparing to other projects, these would be in separate tests... repo and gateway)
        var dbEntity = repo.findById(preUpdateCat.getId().getValue()).get();
        assertEquals(1, repo.count());
        assertEquals(name, dbEntity.getName());
        assertEquals(description, dbEntity.getDescription());
        assertEquals(isActive, dbEntity.isActive());
        assertEquals(preUpdateCat.getId().getValue(), dbEntity.getId());
        assertEquals(preUpdateCat.getCreatedAt(), dbEntity.getCreatedAt());
        assertTrue(preUpdateCat.getUpdatedAt().isBefore(dbEntity.getUpdatedAt()));
        assertNull(dbEntity.getDeletedAt());

    }

    @Test
    public void givenValidCategoryId_whenCallsDeleteById_shouldDeleteCategory() {
        final var name = "filmes";
        final var description = "desc";
        final var isActive = true;
        final var cat = Category.newCategory(name, description, isActive);

        assertEquals(0, repo.count()); //repo is empty

        final var found = repo.saveAndFlush(CategoryJpaEntity.from(cat));
        //asserts initial value is saved correctly
        assertEquals(1, repo.count());

        gateway.deleteById(cat.getId());
        assertEquals(0, repo.count()); //no new entries created
    }

    @Test
    public void givenInvalidCategoryId_whenCallsDeleteById_shouldBeOk() {
        assertEquals(0, repo.count()); //repo is empty
        gateway.deleteById(CategoryID.from("invalid id"));
        assertEquals(0, repo.count()); //no new entries created
    }

    @Test
    public void givenExistingCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var name = "filmes";
        final var description = "desc";
        final var isActive = true;
        final var persisted = Category.newCategory(name, description, isActive);

        assertEquals(0, repo.count()); //repo is empty

        repo.saveAndFlush(CategoryJpaEntity.from(persisted));
        var oldDbEntity = repo.findById(persisted.getId().getValue()).get();
        //asserts initial value is saved correctly
        assertEquals(1, repo.count());
        assertEquals(persisted.getId().getValue(), oldDbEntity.getId());
        assertEquals(persisted.getName(), oldDbEntity.getName());

        final var found = gateway.findById(persisted.getId()).get();
        assertEquals(1, repo.count()); //no new entries created

        //this tests what comes through the gateway
        assertEquals(name, found.getName());
        assertEquals(description, found.getDescription());
        assertEquals(isActive, found.isActive());
        assertEquals(persisted.getId(), found.getId());
        assertEquals(persisted.getCreatedAt(), found.getCreatedAt());
        assertEquals(persisted.getUpdatedAt(), found.getUpdatedAt());
        assertNull(found.getDeletedAt());

        //this tests reading directly from the repo (comparing to other projects, these would be in separate tests... repo and gateway)
        var dbEntity = repo.findById(persisted.getId().getValue()).get();
        assertEquals(1, repo.count());
        assertEquals(name, dbEntity.getName());
        assertEquals(description, dbEntity.getDescription());
        assertEquals(isActive, dbEntity.isActive());
        assertEquals(persisted.getId().getValue(), dbEntity.getId());
        assertEquals(persisted.getUpdatedAt(), dbEntity.getUpdatedAt());
        assertEquals(persisted.getCreatedAt(), dbEntity.getCreatedAt());
        assertNull(dbEntity.getDeletedAt());

    }

    @Test
    public void givenNonExistingCategoryId_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, repo.count()); //repo is empty

        final var found = gateway.findById(CategoryID.from("non existent id"));

        assertTrue(found.isEmpty());
    }

    @Test
    public void givenExistingCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTotal = 3;
        final var expectedItemsCount = expectedPerPage;

        final var cat1 = Category.newCategory("filmes", null, true);
        final var cat2 = Category.newCategory("filmes2", null, true);
        final var cat3 = Category.newCategory("filmes3", null, true);

        assertEquals(0, repo.count());
        repo.saveAll(List.of(CategoryJpaEntity.from(cat1), CategoryJpaEntity.from(cat2), CategoryJpaEntity.from(cat3)));
        assertEquals(3, repo.count());

        final var query = new CategorySearchQuery(0, 2, "", "name", "asc");
        final var result = gateway.findAll(query);

        assertEquals(expectedPage, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(expectedTotal, result.total());
        assertEquals(expectedItemsCount, result.items().size()); // == expectecPerPage
        assertEquals(cat1.getId(), result.items().get(0).getId());
        assertEquals(cat2.getId(), result.items().get(1).getId());

    }

    @Test
    public void givenNoCategories_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTotal = 0;
        final var expectedItemsCount = 0;

        assertEquals(0, repo.count());

        final var query = new CategorySearchQuery(0, 2, "", "name", "asc");
        final var result = gateway.findAll(query);

        assertEquals(expectedPage, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(expectedTotal, result.total());
        assertEquals(expectedItemsCount, result.items().size()); // == expectecPerPage
    }

    @Test
    public void givenExistingCategories_whenCallsFindAllWithDifferentQueries_shouldReturnPaginated() {
        final var expectedPerPage = 2;
        final var expectedTotal = 3;
        final var expectedItemsCount = expectedPerPage;

        final var cat1 = Category.newCategory("filmes", "az", true);
        final var cat2 = Category.newCategory("filmes2", "bz", true);
        final var cat3 = Category.newCategory("zdocumentarios", "cz", true);

        assertEquals(0, repo.count());
        repo.saveAll(List.of(CategoryJpaEntity.from(cat1), CategoryJpaEntity.from(cat2), CategoryJpaEntity.from(cat3)));
        assertEquals(3, repo.count());

        var query = new CategorySearchQuery(0, 2, "", "name", "asc");
        var result = gateway.findAll(query);

        assertEquals(0, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(expectedTotal, result.total());
        assertEquals(expectedItemsCount, result.items().size()); // == expectecPerPage
        assertEquals(cat1.getId(), result.items().get(0).getId());
        assertEquals(cat2.getId(), result.items().get(1).getId());

        query = new CategorySearchQuery(1, 2, "", "name", "asc");
        result = gateway.findAll(query);

        assertEquals(1, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(expectedTotal, result.total());
        assertEquals(1, result.items().size()); // == expectecPerPage
        assertEquals(cat3.getId(), result.items().get(0).getId());

        query = new CategorySearchQuery(0, 2, "doc", "name", "asc");
        result = gateway.findAll(query);

        assertEquals(0, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(1, result.total());
        assertEquals(1, result.items().size()); // == expectecPerPage
        assertEquals(cat3.getId(), result.items().get(0).getId());

        query = new CategorySearchQuery(0, 2, "az", "name", "asc");
        result = gateway.findAll(query);

        assertEquals(0, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(1, result.total());
        assertEquals(1, result.items().size()); // == expectecPerPage
        assertEquals(cat1.getId(), result.items().get(0).getId());

    }
}
