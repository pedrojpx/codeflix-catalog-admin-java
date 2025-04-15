package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get;

import com.pedrojpx.codeflix.admin.catalog.IntegrationTest;
import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetCategoryByIdUseCaseTestIT {
    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository repo;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void testInjection() {
        assertNotNull(useCase);
        assertNotNull(repo);
    }

    @Test
    public void givenValidInput_whenCalled_ShouldReturnCategory() {
        final var c = Category.newCategory("will be retrieved", "retrieved", true);
        final var expectedId = c.getId();
        final var expectedName = c.getName();
        final var expectedDescription = c.getDescription();
        final var expectedIsActive = c.isActive();

        assertEquals(0, repo.count());
        save(c);
        assertEquals(1, repo.count());

        final var found = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId,found.id());
        Assertions.assertEquals(expectedName,found.name());
        Assertions.assertEquals(expectedDescription,found.description());
        Assertions.assertEquals(expectedIsActive,found.isActive());

    }

    @Test
    public void givenInvalidId_whenCalled_ShouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category id ID 123 not found";

        final var exception = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        verify(gateway, times(1)).findById(eq(expectedId));

    }

    @Test
    public void givenValidInput_whenGatewayThrowException_ShouldReturnException() {
        final var c = Category.newCategory("will be retrieved", "retrieved", true);
        final var expectedId = c.getId();
        final var expectedErrorMessage = "Gateway Error";


        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).findById(eq(expectedId));

        final var exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        verify(gateway, times(1)).findById(eq(expectedId));
    }

    private void save(Category... cats) {
        repo.saveAllAndFlush(Arrays.stream(cats).map(CategoryJpaEntity::from).toList());
    }
}
