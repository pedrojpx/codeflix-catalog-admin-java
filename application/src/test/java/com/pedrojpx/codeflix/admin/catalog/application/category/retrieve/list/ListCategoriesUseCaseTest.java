package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.list;

import com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.pedrojpx.codeflix.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategorySearchQuery;
import com.pedrojpx.codeflix.admin.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenValidInput_whenCalled_ShouldReturnCategoryList() {
        final var list = List.of(
            Category.newCategory("will be retrieved", "retrieved", true),
            Category.newCategory("will be retrieved", "retrieved", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, list.size(), list);

        final var expectedItemCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListItemOutput::from);

        when(gateway.findAll(eq(query))).thenReturn(expectedPagination);

        final var found = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, found.items().size());
        Assertions.assertEquals(expectedResult, found);
        Assertions.assertEquals(expectedPage, found.currentPage());
        Assertions.assertEquals(expectedPerPage, found.perPage());
        Assertions.assertEquals(list.size(),found.total());
    }

    @Test
    public void givenValidInput_whenHasNotResults_ShouldReturnEmptyList() {
        final var list = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, list.size(), list);

        final var expectedItemCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListItemOutput::from);

        when(gateway.findAll(eq(query))).thenReturn(expectedPagination);

        final var found = useCase.execute(query);

        Assertions.assertEquals(expectedItemCount, found.items().size());
        Assertions.assertEquals(expectedResult, found);
        Assertions.assertEquals(expectedPage, found.currentPage());
        Assertions.assertEquals(expectedPerPage, found.perPage());
        Assertions.assertEquals(list.size(),found.total());

    }

    @Test
    public void givenValidInput_whenGatewayThrowException_ShouldReturnException() {
        final var expectedErrorMessage = "Gateway Error";

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(gateway.findAll(eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(query)
        );

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        verify(gateway, times(1)).findAll(eq(query));
    }
}
