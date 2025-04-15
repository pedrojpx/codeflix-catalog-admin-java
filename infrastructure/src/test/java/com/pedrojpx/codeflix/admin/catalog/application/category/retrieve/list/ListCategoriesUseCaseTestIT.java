package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list;

import com.pedrojpx.codeflix.admin.catalog.IntegrationTest;
import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class ListCategoriesUseCaseTestIT {
    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository repo;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void testInjection() {
        assertNotNull(useCase);
        assertNotNull(repo);
    }

    @BeforeEach
    void mockUp() {
        final var cats = Stream.of(
                Category.newCategory("filmes", "", true),
                Category.newCategory("originals", "titulos de autoria original", true),
                Category.newCategory("documentarios", "", true),
                Category.newCategory("esportes", "", true),
                Category.newCategory("kids", "", true),
                Category.newCategory("series", "", true)
        ).map(CategoryJpaEntity::from).toList();
        this.repo.saveAllAndFlush(cats);
    }

    @Test
    public void givenValidTerm_whenTermNotMatch_shouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "zzzzzzzzzzzzzzzzzzz";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage,expectedTerms, expectedSort, expectedDirection);
        final var result = useCase.execute(query);

        assertEquals(expectedItemsCount, result.items().size());
        assertEquals(expectedPage, result.currentPage());
        assertEquals(expectedPerPage, result.perPage());
        assertEquals(expectedTotal, result.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,filmes",
            "ginals,0,10,1,1,originals",
            "kids,0,10,1,1,kids",
            "autoria,0,10,1,1,originals",
    })
    public void givenValidTerm_whenMatch_shouldReturnCategores(
            final String terms,
            final int page,
            final int perPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(page, perPage,terms, expectedSort, expectedDirection);
        final var result = useCase.execute(query);

        assertEquals(expectedItemsCount, result.items().size());
        assertEquals(page, result.currentPage());
        assertEquals(perPage, result.perPage());
        assertEquals(expectedTotal, result.total());
        assertEquals(expectedCategoryName, result.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,6,6,documentarios",
            "name,desc,0,10,6,6,series",
            "createdAt,asc,0,10,6,6,filmes",
            "createdAt,desc,0,10,6,6,series",
    })
    public void givenValidSortAndDir_whenCalled_thenShouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int page,
            final int perPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var terms = "";
        final var query = new CategorySearchQuery(page, perPage,terms, expectedSort, expectedDirection);
        final var result = useCase.execute(query);

        assertEquals(expectedItemsCount, result.items().size());
        assertEquals(page, result.currentPage());
        assertEquals(perPage, result.perPage());
        assertEquals(expectedTotal, result.total());
        assertEquals(expectedCategoryName, result.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,6,documentarios;esportes",
            "1,2,2,6,filmes;kids",
            "2,2,2,6,originals;series",
    })
    public void givenValidPage_whenCalled_thenShouldThePage(
            final int page,
            final int perPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryNames
    ) {
        final var terms = "";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage,terms, sort, direction);
        final var result = useCase.execute(query);

        assertEquals(expectedItemsCount, result.items().size());
        assertEquals(page, result.currentPage());
        assertEquals(perPage, result.perPage());
        assertEquals(expectedTotal, result.total());

        int index = 0;
        for (final String expectedName : expectedCategoryNames.split(";")) {
            String name = result.items().get(index).name();
            assertEquals(expectedName, name);
            index++;
        }
    }
}
