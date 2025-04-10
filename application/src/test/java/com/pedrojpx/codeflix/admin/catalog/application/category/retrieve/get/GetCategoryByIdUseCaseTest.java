package com.pedrojpx.codeflix.admin.catalog.application.category.retrieve.get;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryID;
import com.pedrojpx.codeflix.admin.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenValidInput_whenCalled_ShouldReturnCategory() {
        final var c = Category.newCategory("will be retrieved", "retrieved", true);
        final var expectedId = c.getId();
        final var expectedName = c.getName();
        final var expectedDescription = c.getDescription();
        final var expectedIsActive = c.isActive();

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(c.clone()));

        final var found = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(CategoryOutput.from(c),found);
        Assertions.assertEquals(expectedId,found.id());
        Assertions.assertEquals(expectedName,found.name());
        Assertions.assertEquals(expectedDescription,found.description());
        Assertions.assertEquals(expectedIsActive,found.isActive());
        Assertions.assertEquals(c.getCreatedAt(),found.createdAt());
        Assertions.assertEquals(c.getUpdatedAt(),found.updatedAt());
        Assertions.assertEquals(c.getDeletedAt(),found.deletedAt());

    }

    @Test
    public void givenInvalidId_whenCalled_ShouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category id ID 123 not found";

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

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


        when(gateway.findById(eq(expectedId))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        verify(gateway, times(1)).findById(eq(expectedId));
    }


}
