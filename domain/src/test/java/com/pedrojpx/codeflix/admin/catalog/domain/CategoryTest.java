package com.pedrojpx.codeflix.admin.catalog.domain;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.pedrojpx.codeflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() { //given... when... then... test naming convention
        final var expectedName = "Filmes";
        final var expectedDescription = "A category mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenInvalidParams_whenCallNewCategory_thenReportError() { //given... when... then... test naming convention
        final String expectedName = null;
        final var expectedDescription = "A category mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

    }


}
