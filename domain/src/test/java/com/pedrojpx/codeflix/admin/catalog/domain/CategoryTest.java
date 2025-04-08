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
    public void givenNullName_whenCallNewCategory_thenReportError() { //given... when... then... test naming convention
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

    @Test
    public void givenEmptyName_whenCallNewCategory_thenReportError() { //given... when... then... test naming convention
        final String expectedName = "";
        final var expectedDescription = "A category mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

    }

    @Test
    public void givenTooShortNameLength_whenCallNewCategory_thenReportError() { //given... when... then... test naming convention
        final String expectedName = "Fi "; //min 3 characters, space should not be counted
        final var expectedDescription = "A category mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

    }

    @Test
    public void givenTooLongNameLength_whenCallNewCategory_thenReportError() { //given... when... then... test naming convention
        final String expectedName = """
            Lorem ipsum dictumst fusce congue aptent purus curae, placerat diam phasellus porta ullamcorper ultrices vehicula vel, cras est lectus integer non placerat. donec curabitur ultrices etiam donec metus proin potenti conubia felis condimentum, ad faucibus ele
            """; //max 255 characters, space should not be counted
        final var expectedDescription = "A category mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

    }

    @Test
    public void givenEmptyDescription_whenCallNewCategory_thenOK() { //given... when... then... test naming convention
        final String expectedName = "name"; //max 255 characters, space should not be counted
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

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
    public void givenIsActiveFalse_whenCallNewCategory_thenOK() { //given... when... then... test naming convention
        final String expectedName = "name"; //max 255 characters, space should not be counted
        final var expectedDescription = "";
        final var expectedIsActive = false;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnActiveCategory_whenCalledDeactivate_thenCategoryIsDeactivated(){
        final String expectedName = "name"; //max 255 characters, space should not be counted
        final var expectedDescription = "description";
        final var expectedIsActive = true;

        final var category =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final var deactivated = category.deactivate();

        Assertions.assertEquals(deactivated, category);
        Assertions.assertEquals(deactivated.getName(), category.getName());
        Assertions.assertEquals(deactivated.getDescription(), category.getDescription());
        Assertions.assertEquals(deactivated.getCreatedAt(), createdAt);
        Assertions.assertFalse(deactivated.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertTrue(deactivated.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAnInactiveCategory_whenCalledActivate_thenCategoryIsActivated(){
        final String expectedName = "name"; //max 255 characters, space should not be counted
        final var expectedDescription = "description";

        final var category =
                Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final var activated = category.activate();

        Assertions.assertEquals(activated, category);
        Assertions.assertEquals(activated.getName(), category.getName());
        Assertions.assertEquals(activated.getDescription(), category.getDescription());
        Assertions.assertTrue(activated.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertTrue(activated.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(activated.getDeletedAt());
        Assertions.assertEquals(activated.getCreatedAt(), createdAt);
    }




}
