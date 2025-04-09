package com.pedrojpx.codeflix.admin.catalog.application.category.delete;

import com.pedrojpx.codeflix.admin.catalog.domain.category.Category;
import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        reset(gateway);
    }

    //1. caminho feliz
    //2. deletar id que não existe
    //3. gateway error

    @Test
    public void givenValidInput_whenCallDelete_ShouldBeOK() {
        final var c = Category.newCategory("will be deleted", "", true);
        final var expectedId = c.getId();

        doNothing().when(gateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(gateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidInput_whenCallDelete_ShouldBeOK() {
        final var c = Category.newCategory("will be deleted", "", true);
        final var expectedId = c.getId();

        doNothing().when(gateway).deleteById(eq(expectedId));

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
