package com.pedrojpx.codeflix.admin.catalog.application.category.update;

import com.pedrojpx.codeflix.admin.catalog.IntegrationTest;
import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.pedrojpx.codeflix.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseTestIT {
    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repo;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void testInjection() {
        assertNotNull(useCase);
        assertNotNull(repo);
    }

    private void save(Category... cats) {
        repo.saveAllAndFlush(Arrays.stream(cats).map(CategoryJpaEntity::from).toList());
    }

    @Test
    public void givenValidInput_whenCallsUpdate_shouldReturnCategoryId() {
        final var c = Category.newCategory("will be updated", "", true);
        final var expectedId = c.getId();
        final var expectedName = "filmes";
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, repo.count());
        save(c);
        assertEquals(1, repo.count());

        final var output = useCase.execute(input).get();
        final var found = repo.findById(output.id().getValue()).get();

        assertNotNull(output);
        assertNotNull(output.id());
        verify(gateway, times(1)).findById(eq(expectedId));
        assertEquals(expectedId.getValue(), found.getId());
        assertEquals(expectedName, found.getName());
        assertEquals(expectedDescription, found.getDescription());
        assertEquals(expectedIsActive, found.isActive());
        assertTrue(c.getUpdatedAt().isBefore(found.getUpdatedAt()));
        assertNull(found.getDeletedAt());
    }

    @Test
    public void givenInvalidInput_whenCallsUpdate_shouldReturnDomainException() {
        final var c = Category.newCategory("will be updated", "", true);
        save(c);
        final var expectedId = c.getId();
        final String expectedName = null;
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(input).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }

    @Test
    public void givenValidInputtoInactivate_whenCallsUpdate_shouldReturnCategoryId() {
        final var c = Category.newCategory("will be updated", "", true);
        assertTrue(c.isActive());
        assertNull(c.getDeletedAt());
        save(c);

        final var expectedId = c.getId();
        final var expectedName = "filmes";
        final var expectedDescription = "descrição";
        final var expectedIsActive = false;
        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var output = useCase.execute(input).get();
        final var found = repo.findById(c.getId().getValue()).get();

        assertNotNull(output);
        assertNotNull(output.id());
        assertEquals(expectedId, output.id());
        assertEquals(expectedId.getValue(), found.getId());
        assertFalse(found.isActive());
        verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenValidInput_whenGatewayErrors_shouldReturnCategoryId() {
        final var c = Category.newCategory("will be updated", "", true);
        save(c);
        final var expectedId = c.getId();
        final var expectedName = "filmes";
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).update(any());

        final var output = useCase.execute(input).getLeft();

        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage, output.firstError().message());
        verify(gateway, times(1)).findById(eq(expectedId));

        //asserts nothing is changed:
        final var found = repo.findById(c.getId().getValue()).get();
        assertEquals(c.getId().getValue(), found.getId());
        assertEquals(c.getName(), found.getName());
        assertEquals(c.getDescription(), found.getDescription());
        assertEquals(c.isActive(), found.isActive());
    }

    @Test
    public void givenInvalidCategoryId_whenCallsUpdate_shouldReturnDomainException() {
        final var expectedId = CategoryID.from("123");
        final String expectedName = "Filme";
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category ID 123 was not found";

        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(input));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }
}
