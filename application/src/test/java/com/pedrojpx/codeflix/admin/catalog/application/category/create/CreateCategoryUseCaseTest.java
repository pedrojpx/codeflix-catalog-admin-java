package com.pedrojpx.codeflix.admin.catalog.application.category.create;

import com.pedrojpx.codeflix.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

public class CreateCategoryUseCaseTest {

    //1. teste do caminho feliz
    //2. teste com propriedade invalida (name)
    //3. teste criando uma categoria inativa
    //4. teste simulando um erro genérico vindo do gateway

    @Test
    public void givenValidCommand_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de Filmes";
        final var expectedIsActive = true;

        final var input = CreateCategoryInput.with(expectedName, expectedDescription, expectedIsActive);

        final CategoryGateway gateway = Mockito.mock(CategoryGateway.class);
        //gateway is made to return the argument sent so that we can check if the arguments passed were correct
        //(remember we are testing the `UseCase` here! Not the gateway!
        Mockito.when(gateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(gateway);

        final var output = useCase.execute(input);

        assertNotNull(output);
        assertNotNull(output.id());

        //checks that `create` was called once AND with the following data
        Mockito.verify(gateway, Mockito.times(1)).create(argThat(category ->
                     Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.isNull(category.getDeletedAt())
        ));
    }
}
