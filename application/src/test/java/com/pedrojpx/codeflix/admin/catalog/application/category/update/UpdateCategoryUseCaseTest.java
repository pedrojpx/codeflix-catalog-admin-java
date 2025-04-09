package com.pedrojpx.codeflix.admin.catalog.application.category.update;

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

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase usecase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset();
    }

    // 1. teste caminho feliz
    // 2. teste passando propriedade inválida
    // 3. teste atualizando uma categoria para inativa
    // 4. teste simulando um erro genérico vindo do gateway
    // 5. atualizar categoria passando id invalido

    @Test
    public void givenValidInput_whenCallsUpdate_shouldReturnCategoryId() {
        final var c = Category.newCategory("will be updated", "", true);
        final var expectedId = c.getId();
        final var expectedName = "filmes";
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(c.clone()));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = usecase.execute(input).get();

        assertNotNull(output);
        assertNotNull(output.id());
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(1)).update(argThat(updated ->
                Objects.equals(expectedName, updated.getName())
                        && Objects.equals(expectedDescription, updated.getDescription())
                        && Objects.equals(expectedIsActive, updated.isActive())
                        && Objects.nonNull(updated.getId())
                        && Objects.equals(c.getCreatedAt(), updated.getCreatedAt())
                        && c.getUpdatedAt().isBefore(updated.getUpdatedAt())
                        && Objects.isNull(updated.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidInput_whenCallsUpdate_shouldReturnDomainException() {
        final var c = Category.newCategory("will be updated", "", true);
        final var expectedId = c.getId();
        final String expectedName = null;
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(c.clone()));

        final var notification = usecase.execute(input).getLeft();

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

        final var expectedId = c.getId();
        final var expectedName = "filmes";
        final var expectedDescription = "descrição";
        final var expectedIsActive = false;
        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(c.clone()));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = usecase.execute(input).get();

        assertNotNull(output);
        assertNotNull(output.id());
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(1)).update(argThat(updated ->
                Objects.equals(expectedName, updated.getName())
                        && Objects.equals(expectedDescription, updated.getDescription())
                        && Objects.equals(expectedIsActive, updated.isActive())
                        && Objects.nonNull(updated.getId())
                        && Objects.equals(c.getCreatedAt(), updated.getCreatedAt())
                        && c.getUpdatedAt().isBefore(updated.getUpdatedAt())
                        && Objects.nonNull(updated.getDeletedAt())
        ));
    }

    @Test
    public void givenValidInput_whenGatewayErrors_shouldReturnCategoryId() {
        final var c = Category.newCategory("will be updated", "", true);
        final var expectedId = c.getId();
        final var expectedName = "filmes";
        final var expectedDescription = "descrição";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var input = UpdateCategoryInput.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.of(c.clone()));
        when(gateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var output = usecase.execute(input).getLeft();

        assertEquals(expectedErrorCount, output.getErrors().size());
        assertEquals(expectedErrorMessage, output.firstError().message());
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(1)).update(argThat(updated ->
                Objects.equals(expectedName, updated.getName())
                        && Objects.equals(expectedDescription, updated.getDescription())
                        && Objects.equals(expectedIsActive, updated.isActive())
                        && Objects.nonNull(updated.getId())
                        && Objects.equals(c.getCreatedAt(), updated.getCreatedAt())
                        && c.getUpdatedAt().isBefore(updated.getUpdatedAt())
                        && Objects.isNull(updated.getDeletedAt())
        ));
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

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(DomainException.class, () -> usecase.execute(input));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }

}
