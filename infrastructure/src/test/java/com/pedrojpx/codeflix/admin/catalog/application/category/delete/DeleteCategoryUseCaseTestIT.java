package com.pedrojpx.codeflix.admin.catalog.application.category.delete;

import com.pedrojpx.codeflix.admin.catalog.IntegrationTest;
import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseTestIT {
    @Autowired
    private DeleteCategoryUseCase useCase;

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
    public void givenValidInput_whenCallDelete_ShouldBeOK() {
        final var c = Category.newCategory("will be deleted", "", true);
        final var expectedId = c.getId();

        assertEquals(0, repo.count());
        repo.save(CategoryJpaEntity.from(c));
        assertEquals(1, repo.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, repo.count());

        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidInput_whenCallDelete_ShouldBeOK() {
        final var c = Category.newCategory("will be deleted", "", true);
        final var expectedId = c.getId();
        assertEquals(0, repo.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenValidInput_whenGatewayErrors_ShouldReturnException() {
        final var c = Category.newCategory("will be deleted", "", true);
        final var expectedId = c.getId();

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(eq(expectedId));
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }
}
